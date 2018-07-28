package com.brentcroft.util.templates.jstl.tag;

import java.util.Map;

import javax.el.ValueExpression;

import com.brentcroft.util.templates.ELTemplateManager;
import com.brentcroft.util.templates.jstl.JstlTemplate;
import com.brentcroft.util.tools.MapBindings;

public class JstlWhen extends AbstractJstlElement
{
    private final static String TAG = "c:when";

    protected String testEL;

    protected ValueExpression valueExpression;

    private final ELTemplateManager elTemplateManager;

    public JstlWhen( ELTemplateManager elTemplateManager, String testEL )
    {
        this.elTemplateManager = elTemplateManager;

        this.testEL = testEL;


        valueExpression = null;
        innerRenderable = new JstlTemplate( this );
    }

    private void compile()
    {
        valueExpression = elTemplateManager.getValueExpression( testEL, EMPTY_MAP, Boolean.class );
    }

    @Override
    public void normalize()
    {
        compile();
    }


    public boolean test( Map<?, ?> rootObjects )
    {
        final Object value = valueExpression.getValue( elTemplateManager.getELContext( rootObjects ) );

        return ( value instanceof Boolean ) && (Boolean) value;
    }


    public String render( Map<String, ? super Object> bindings )
    {
        // protect external bindings from pollution in local scope
        return innerRenderable.render( new MapBindings( bindings ) );
    }

    public String toText()
    {
        return String.format( "<%s test=\"%s\">%s</%s>", TAG, testEL, innerRenderable, TAG );
    }
}