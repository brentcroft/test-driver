package com.brentcroft.gtd.camera;

import static com.brentcroft.util.XmlUtils.getClassIdentifier;
import static java.lang.String.format;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.brentcroft.gtd.adapter.model.AbstractGuiObjectAdapter;
import com.brentcroft.gtd.adapter.model.GuiObject;
import com.brentcroft.gtd.adapter.model.GuiObjectAdapter;
import com.brentcroft.gtd.adapter.model.GuiObjectConsultant;
import com.brentcroft.gtd.adapter.utils.HashCacheImpl;
import com.brentcroft.gtd.adapter.utils.ReflectionUtils;
import com.brentcroft.gtd.driver.GuiObjectManager;
import com.brentcroft.gtd.driver.utils.HashCache;
import com.brentcroft.util.xpath.gob.Gob;

/**
 * Created by Alaric on 14/07/2017.
 */
public class CameraObjectManager implements GuiObjectManager< GuiObject< ? > >
{
	private static final Logger logger = Logger.getLogger( CameraObjectManager.class.getName() );

	private final HashCache< GuiObject< ? > > hashCache = new HashCacheImpl<>();

	private final static Comparator< GuiObjectAdapter< ? > > COMPARATOR = ( h1, h2 ) -> {
		try
		{
			return h1.getOrderKey().compareTo( h2.getOrderKey() );
		}
		catch ( Exception e )
		{
			throw new IllegalStateException( format( "Error comparing keys: h1=[%s], h2=[%s].", h1, h2 ), e );
		}
	};

	private final Map< Class< ? >, GuiObjectAdapter< ? > > adaptersByClass = new LinkedHashMap<>();
	private final Map< Class< ? >, GuiObjectAdapter< ? > > usedAdaptersByClass = new HashMap<>();

	private final List< GuiObjectAdapter< ? > > adaptersByRank = new ArrayList<>();

	public void configure( Properties properties )
	{
	}

	public void clear()
	{
		usedAdaptersByClass.clear();
		adaptersByClass.clear();
		adaptersByRank.clear();
		hashCache.gc();
	}

	public void clean()
	{
		usedAdaptersByClass.clear();
		hashCache.gc();
	}

	@Override
	public HashCache< GuiObject< ? > > getHashCache()
	{
		return hashCache;
	}

	@Override
	public GuiObject< ? > adapt( Object object, Gob parent )
	{
		return findAdapter( object ).adapt( object, parent );
	}

	public void addAdapter( GuiObjectAdapter< ? > adapter )
	{
		adaptersByClass.put( adapter.handler(), adapter );

		linkSuperAdapters();
	}

	public void addAdapters( Collection< ? extends GuiObjectAdapter< ? > > newAdapters )
	{
		newAdapters.forEach( adapter -> {
			GuiObjectAdapter< ? > replaced = adaptersByClass.put( adapter.handler(), adapter );

			if ( replaced != null )
			{
				logger.info( format( "Replacing adapter: new=[%s], old=[%s], handler=[%s].", adapter, replaced,
						adapter.handler() ) );
			}

		} );

		linkSuperAdapters();
	}

	/**
	 * For each adapter, iterates over all the other adapters, excluding an already
	 * assigned super adapter, or any candidate who's handler is not assignable from
	 * the adapter's handler, resolving the least super candidate which is assigned
	 * to the adapter.
	 */
	private void linkSuperAdapters()
	{
		// distribute consultants

		// iterates over adapters * adapters
		adaptersByClass
				.values()
				.stream()
				.forEach( adapter -> {
					adaptersByClass
							.values()
							.stream()
							.filter( candidate -> !candidate.equals( adapter ) )
							.filter( candidate -> candidate != adapter.getSuperAdapter() )
							.filter( candidate -> candidate.handler().isAssignableFrom( adapter.handler() ) )
							.forEach( candidate -> {
								// must be sequential
								// any candidate might make an improvement - less super
								if ( (adapter.getSuperAdapter() == null)
										// switcheroo - we want the least super
										|| adapter.getSuperAdapter().handler().isAssignableFrom( candidate.handler() ) )
								{
									adapter.setSuperAdapter( candidate );
								}
							} );
				} );

		adaptersByRank.clear();

		adaptersByRank.addAll( adaptersByClass.values() );

		adaptersByRank.sort( COMPARATOR );

		// ripple up consultants
		// in reverse order
		adaptersByRank
				.stream()
				.collect(
						Collector.of( ArrayDeque::new, ( deq, t ) -> deq.addFirst( t ),
								( d1, d2 ) -> {
									d2.addAll( d1 );
									return d2;
								} ) )
				.stream()
				.map( adapter -> ( GuiObjectAdapter< ? > ) adapter )
				.filter( adapter -> adapter.getConsultant() == null )
				.filter( adapter -> adapter.getSuperAdapter() != null )
				// since sorted
				.forEach( adapter -> adapter.setConsultant( adapter.getSuperAdapter().getConsultant() ) );

		// TODO: half the previous code is meant to isolate this array from being
		// resorted
		adaptersByRank.sort( COMPARATOR );

		usedAdaptersByClass.clear();
	}

	@SuppressWarnings( "unchecked" )
	private < T > GuiObjectAdapter< ? super T > findAdapter( T t )
	{
		GuiObjectAdapter< ? super T > specificHandler = ( GuiObjectAdapter< ? super T > ) usedAdaptersByClass.get( t.getClass() );

		if ( specificHandler != null )
		{
			return specificHandler;
		}

		// then walk through the handlers by rank
		for ( GuiObjectAdapter< ? > adapter : adaptersByRank )
		{
			if ( adapter.handles( t ) )
			{
				// late entry - avoid walk again
				usedAdaptersByClass.put( t.getClass(), adapter );

				return ( GuiObjectAdapter< ? super T > ) adapter;
			}
		}

		throw new RuntimeException( format(
				"Cannot adapt type [%s]; %s",
				t == null ? null : t.getClass().getName(), t ) );
	}

	public < C, H extends GuiObject< ? super C > > void install( List< ? extends AdapterSpecification< C, H > > adapters )
	{
		// addAdapters(
		// adapters
		// .entrySet()
		// .stream()
		// .map( entry -> newAdapter( entry.getKey(), entry.getValue() ) )
		// .collect( Collectors.toList() ) );

		// 1.8
		adapters.stream()
				.map( spec -> ( GuiObjectAdapter< ? > ) newAdapter( spec.adapteeClass, spec.adapterClass, spec.adapterGuiObjectConsultant ) )
				// .forEach( adapter -> {
				// adaptersByClass.put( adapter.handler(), adapter );
				// } );
				.forEach( adapter -> addAdapter( adapter ) );
		// 1.9
		// .forEach( this::addAdapter );

		linkSuperAdapters();
	}

	public < C, H extends GuiObject< ? super C > > AdapterSpecification< C, H > newAdapterSpecification(
			Class< C > adapteeClass, Class< H > adapterClass )
	{
		return new AdapterSpecification< C, H >( adapteeClass, adapterClass, null );
	}

	public < C, H extends GuiObject< ? super C > > AdapterSpecification< C, H > newAdapterSpecification(
			Class< C > adapteeClass, Class< H > adapterClass, GuiObjectConsultant< C > adapterGuiObjectConsultant )
	{
		return new AdapterSpecification< C, H >( adapteeClass, adapterClass, adapterGuiObjectConsultant );
	}

	public class AdapterSpecification< C, H extends GuiObject< ? super C > >
	{
		public Class< C > adapteeClass;
		public Class< H > adapterClass;
		public GuiObjectConsultant< C > adapterGuiObjectConsultant;

		public AdapterSpecification( Class< C > adapteeClass, Class< H > adapterClass,
				GuiObjectConsultant< C > adapterGuiObjectConsultant )
		{
			this.adapteeClass = adapteeClass;
			this.adapterClass = adapterClass;
			this.adapterGuiObjectConsultant = adapterGuiObjectConsultant;
		}
	}

	private < C, H extends GuiObject< ? super C > > GuiObjectAdapter< C > newAdapter(
			Class< C > adapteeClass,
			Class< H > adapterClass,
			GuiObjectConsultant< C > adapterGuiObjectConsultant )
	{
		return new AbstractGuiObjectAdapter< C >( adapteeClass )
		{
			private Constructor< H > constructor;

			{
				constructor = ReflectionUtils.findConstructor(
						adapterClass,
						adapteeClass,
						Gob.class,
						GuiObjectConsultant.class,
						CameraObjectManager.class );

				if ( constructor == null )
				{
					throw new RuntimeException( "No constructor found for args." );
				}

				setConsultant( adapterGuiObjectConsultant );
			}

			@Override
			public H adapt( C c, Gob parent )
			{
				try
				{
					return constructor.newInstance( c, parent, getConsultant(), CameraObjectManager.this );
				}
				catch ( IllegalAccessException | InstantiationException | InvocationTargetException e )
				{
					throw new RuntimeException( format( "Constructor [%s]", constructor ), e );
				}
			}

			@Override
			public String toString()
			{
				return adapterClass.getSimpleName();
			}
		};
	}

	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();

		b.append( format( "%n  %-40s %-40s %-40s %s", "[ adapter ]", "[ super adapter ]", "[ consultant ]",
				"[ handles ]" ) );

		b.append( adaptersByRank.stream()
				.map( adapter -> format( "%n  %-40s %-40s %-40s %s", "(" + adapter.getOrder() + ")" + adapter,
						adapter.getSuperAdapter(),
						adapter.getConsultant() == null ? "null"
								: getClassIdentifier( adapter.getConsultant().getClass() ),
						adapter.handler().getName() ) )
				.collect( Collectors.joining() ) );

		return b.toString();
	}

}
