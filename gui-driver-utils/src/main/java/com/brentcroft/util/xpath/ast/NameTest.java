/* Generated By:JJTree: Do not edit this line. NameTest.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.brentcroft.util.xpath.ast;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

import com.brentcroft.util.xpath.SimpleNode;
import com.brentcroft.util.xpath.XParser;
import com.brentcroft.util.xpath.XParserTreeConstants;
import com.brentcroft.util.xpath.XParserVisitor;
import com.brentcroft.util.xpath.gob.Gob;
import com.brentcroft.util.xpath.gob.Selection;

public class NameTest extends SimpleNode
{
    public NameTest( int id )
    {
        super( id );
    }

    public NameTest( XParser p, int id )
    {
        super( p, id );
    }

    public Selection accept( XParserVisitor visitor, Gob gob, Selection axis )
    {
        //return visitor.visit( this, gob, axis );
        return visit( gob, axis );
    }


    public Selection visit( Gob gob, Selection axis )
    {
        SimpleNode child = getChild( 0 );

        switch ( child.getId() )
        {
            case XParserTreeConstants.JJTWILDCARD:
            case XParserTreeConstants.JJTQNAME:
                return axis.getAxis().getSelection( gob, child.getValue() );

            default:
                throw new IllegalStateException( format( "Unexpected child [%s] of %s: %s", 0, this, child ) );
        }
    }


    List< ? extends Gob > getDescendants( Gob gob )
    {
        List< Gob > descendants = new ArrayList<>();
        if ( gob.hasChildren() )
        {
            privateDescendants( gob, descendants, new IdentityHashMap<>() );
        }
        return descendants;
    }

    void privateDescendants( Gob gob, List< Gob > descendants, final IdentityHashMap< Object, Object > ids )
    {
        ids.put( gob, null );

        if ( gob.hasChildren() )
        {
            gob.getChildren()
                    .stream()
                    .filter( c -> ! ids.containsKey( c ) )
                    .forEach( c -> {
                        descendants.add( c );
                        privateDescendants( c, descendants, ids );
                    } );
        }
    }


}
/* JavaCC - OriginalChecksum=b7da4b8eb7b00c745493bfea538e0987 (do not edit this line) */
