package com.brentcroft.gtd.adapter.model.fx;

import static java.util.Optional.ofNullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.w3c.dom.Element;

import com.brentcroft.gtd.adapter.model.AttrSpec;
import com.brentcroft.gtd.adapter.model.DefaultGuiObject;
import com.brentcroft.gtd.adapter.model.GuiObject;
import com.brentcroft.gtd.adapter.model.GuiObjectConsultant;
import com.brentcroft.gtd.adapter.utils.FXUtils;
import com.brentcroft.gtd.adapter.utils.RobotUtils;
import com.brentcroft.gtd.camera.CameraObjectManager;
import com.brentcroft.util.xpath.gob.Gob;

import javafx.scene.control.MenuItem;

/**
 * Created by Alaric on 14/07/2017.
 */
public class FxMenuItemGuiObject< T extends MenuItem > extends DefaultGuiObject< T > implements GuiObject.Click
{
	public FxMenuItemGuiObject( T t, Gob parent, GuiObjectConsultant< T > guiObjectConsultant,
			CameraObjectManager hgom )
	{
		super( t, parent, guiObjectConsultant, hgom );
	}

	@Override
	public void buildProperties( Element element, Map< String, Object > options )
	{
		super.buildProperties( element, options );

		addRobotAction( element, options );
		addClickAction( element, options );
	}

	@Override
	public void click()
	{
		FXUtils.maybeInvokeNowOnFXThread( getObject()::fire );
	}
	
	@Override
	public void rightClick()
	{
		throw new UnsupportedOperationException();
	}	

	@Override
	public void robotClick()
	{
		RobotUtils.fxRobotClick( getObject() );
	}

	@Override
	public void robotDoubleClick()
	{
		RobotUtils.fxRobotDoubleClick( getObject() );
	}

	// no robot keys

	@SuppressWarnings( "unchecked" )
	@Override
	public List< AttrSpec< T > > loadAttrSpec()
	{
		if ( attrSpec == null )
		{
			attrSpec = super.loadAttrSpec();
			attrSpec.addAll( Arrays.asList( ( AttrSpec< T >[] ) Attr.values() ) );
		}

		return attrSpec;
	}

	enum Attr implements AttrSpec< MenuItem >
	{
		ID( "id", MenuItem::getId ),
		TEXT( "text", go -> ofNullable( go.getText() ).orElse( null ) ),
		VISIBLE( "visible", go -> "" + go.isVisible() ),
		DISABLED( "focus", go -> "" + go.isDisable() );

		final String n;
		final Function< MenuItem, String > f;

		Attr( String name, Function< MenuItem, String > f )
		{
			this.n = name;
			this.f = f;
		}

		@Override
		public String getName()
		{
			return n;
		}

		@Override
		public String getAttribute( MenuItem go )
		{
			return f.apply( go );
		}
	}
}
