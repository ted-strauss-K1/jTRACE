//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.25 at 01:29:02 EDT 
//


package edu.uconn.psy.jtrace.parser;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/Users/tedstrauss/SVN/jtrace/trunk/src/edu/uconn/psy/jtrace/jTRACESchema.xsd line 17)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="script">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice maxOccurs="unbounded">
 *                   &lt;element name="iterate" type="{http://xml.netbeans.org/examples/targetNS}jtIteratorType"/>
 *                   &lt;element name="condition" type="{http://xml.netbeans.org/examples/targetNS}jtConditionalType"/>
 *                   &lt;element name="action" type="{http://xml.netbeans.org/examples/targetNS}jtActionType"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface JtType {


    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDescription();

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDescription(java.lang.String value);

    /**
     * Gets the value of the script property.
     * 
     * @return
     *     possible object is
     *     {@link edu.uconn.psy.jtrace.parser.JtType.ScriptType}
     */
    edu.uconn.psy.jtrace.parser.JtType.ScriptType getScript();

    /**
     * Sets the value of the script property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.uconn.psy.jtrace.parser.JtType.ScriptType}
     */
    void setScript(edu.uconn.psy.jtrace.parser.JtType.ScriptType value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/Users/tedstrauss/SVN/jtrace/trunk/src/edu/uconn/psy/jtrace/jTRACESchema.xsd line 21)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice maxOccurs="unbounded">
     *         &lt;element name="iterate" type="{http://xml.netbeans.org/examples/targetNS}jtIteratorType"/>
     *         &lt;element name="condition" type="{http://xml.netbeans.org/examples/targetNS}jtConditionalType"/>
     *         &lt;element name="action" type="{http://xml.netbeans.org/examples/targetNS}jtActionType"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface ScriptType {


        /**
         * Gets the value of the IterateOrConditionOrAction property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the IterateOrConditionOrAction property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIterateOrConditionOrAction().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link edu.uconn.psy.jtrace.parser.JtType.ScriptType.Action}
         * {@link edu.uconn.psy.jtrace.parser.JtType.ScriptType.Condition}
         * {@link edu.uconn.psy.jtrace.parser.JtType.ScriptType.Iterate}
         * 
         */
        java.util.List getIterateOrConditionOrAction();


        /**
         * Java content class for action element declaration.
         * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/Users/tedstrauss/SVN/jtrace/trunk/src/edu/uconn/psy/jtrace/jTRACESchema.xsd line 25)
         * <p>
         * <pre>
         * &lt;element name="action" type="{http://xml.netbeans.org/examples/targetNS}jtActionType"/>
         * </pre>
         * 
         */
        public interface Action
            extends javax.xml.bind.Element, edu.uconn.psy.jtrace.parser.JtActionType
        {


        }


        /**
         * Java content class for condition element declaration.
         * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/Users/tedstrauss/SVN/jtrace/trunk/src/edu/uconn/psy/jtrace/jTRACESchema.xsd line 24)
         * <p>
         * <pre>
         * &lt;element name="condition" type="{http://xml.netbeans.org/examples/targetNS}jtConditionalType"/>
         * </pre>
         * 
         */
        public interface Condition
            extends javax.xml.bind.Element, edu.uconn.psy.jtrace.parser.JtConditionalType
        {


        }


        /**
         * Java content class for iterate element declaration.
         * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/Users/tedstrauss/SVN/jtrace/trunk/src/edu/uconn/psy/jtrace/jTRACESchema.xsd line 23)
         * <p>
         * <pre>
         * &lt;element name="iterate" type="{http://xml.netbeans.org/examples/targetNS}jtIteratorType"/>
         * </pre>
         * 
         */
        public interface Iterate
            extends javax.xml.bind.Element, edu.uconn.psy.jtrace.parser.JtIteratorType
        {


        }

    }

}