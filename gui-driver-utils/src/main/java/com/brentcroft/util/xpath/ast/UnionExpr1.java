/* Generated By:JJTree: Do not edit this line. UnionExpr1.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.brentcroft.util.xpath.ast;

import com.brentcroft.util.xpath.gob.Gob;
import com.brentcroft.util.xpath.SimpleNode;
import com.brentcroft.util.xpath.XParser;
import com.brentcroft.util.xpath.XParserVisitor;
import com.brentcroft.util.xpath.gob.Selection;

public class UnionExpr1 extends SimpleNode
{
    public UnionExpr1( int id )
    {
        super( id );
    }

    public UnionExpr1( XParser p, int id )
    {
        super( p, id );
    }

    public Selection accept( XParserVisitor visitor, Gob gob, Selection axis )
    {
        return visitor.visit( this, gob, axis );
    }

}
/* JavaCC - OriginalChecksum=ba4ccb0ef5274de5c941f502fa1d98a2 (do not edit this line) */
