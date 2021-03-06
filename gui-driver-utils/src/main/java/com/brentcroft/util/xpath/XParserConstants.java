/* Generated By:JJTree&JavaCC: Do not edit this line. XParserConstants.java */
package com.brentcroft.util.xpath;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface XParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int NumericLiteralError = 39;
  /** RegularExpression Id. */
  int Slash = 40;
  /** RegularExpression Id. */
  int SlashSlash = 41;
  /** RegularExpression Id. */
  int LeftAngleBracket = 42;
  /** RegularExpression Id. */
  int Plus = 43;
  /** RegularExpression Id. */
  int Minus = 44;
  /** RegularExpression Id. */
  int NCNameColonStar = 45;
  /** RegularExpression Id. */
  int IntegerLiteral = 46;
  /** RegularExpression Id. */
  int DecimalLiteral = 47;
  /** RegularExpression Id. */
  int StringLiteral = 48;
  /** RegularExpression Id. */
  int QNameToken = 49;
  /** RegularExpression Id. */
  int NCNameTok = 50;
  /** RegularExpression Id. */
  int Char = 51;
  /** RegularExpression Id. */
  int Digits = 52;
  /** RegularExpression Id. */
  int WhitespaceChar = 53;
  /** RegularExpression Id. */
  int LocalPart = 54;
  /** RegularExpression Id. */
  int Nmstart = 55;
  /** RegularExpression Id. */
  int Nmchar = 56;
  /** RegularExpression Id. */
  int Letter = 57;
  /** RegularExpression Id. */
  int BaseChar = 58;
  /** RegularExpression Id. */
  int Ideographic = 59;
  /** RegularExpression Id. */
  int CombiningChar = 60;
  /** RegularExpression Id. */
  int Digit = 61;
  /** RegularExpression Id. */
  int Extender = 62;
  /** RegularExpression Id. */
  int CommentStart = 63;
  /** RegularExpression Id. */
  int CommentEnd = 64;
  /** RegularExpression Id. */
  int CommentContent = 65;
  /** RegularExpression Id. */
  int ImplicitlyAllowedWhitespace = 66;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int EXPR_COMMENT = 1;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\"or\"",
    "\"and\"",
    "\"=\"",
    "\"!=\"",
    "\">\"",
    "\"<=\"",
    "\">=\"",
    "\"*\"",
    "\"div\"",
    "\"mod\"",
    "\"|\"",
    "\"child\"",
    "\"::\"",
    "\"descendant\"",
    "\"attribute\"",
    "\"self\"",
    "\"descendant-or-self\"",
    "\"following-sibling\"",
    "\"following\"",
    "\"namespace\"",
    "\"@\"",
    "\"parent\"",
    "\"ancestor\"",
    "\"preceding-sibling\"",
    "\"preceding\"",
    "\"ancestor-or-self\"",
    "\"..\"",
    "\"[\"",
    "\"]\"",
    "\"$\"",
    "\"(\"",
    "\")\"",
    "\".\"",
    "\",\"",
    "\"node\"",
    "\"text\"",
    "\"comment\"",
    "\"processing-instruction\"",
    "<NumericLiteralError>",
    "\"/\"",
    "\"//\"",
    "\"<\"",
    "\"+\"",
    "\"-\"",
    "<NCNameColonStar>",
    "<IntegerLiteral>",
    "<DecimalLiteral>",
    "<StringLiteral>",
    "<QNameToken>",
    "<NCNameTok>",
    "<Char>",
    "<Digits>",
    "<WhitespaceChar>",
    "<LocalPart>",
    "<Nmstart>",
    "<Nmchar>",
    "<Letter>",
    "<BaseChar>",
    "<Ideographic>",
    "<CombiningChar>",
    "<Digit>",
    "<Extender>",
    "\"(:\"",
    "\":)\"",
    "<CommentContent>",
    "<ImplicitlyAllowedWhitespace>",
  };

}
