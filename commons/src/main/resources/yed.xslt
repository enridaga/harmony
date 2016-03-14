<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:y="http://www.yworks.com/xml/graphml" xmlns:yed="http://www.yworks.com/xml/yed/3"
	xmlns="http://graphml.graphdrawing.org/xmlns" xmlns:graphml="http://graphml.graphdrawing.org/xmlns"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" indent="yes" encoding="UTF-8" />

	<xsl:template match="node()|@*" name="identity">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*" />
			<xsl:call-template name="enrich"></xsl:call-template>
		</xsl:copy>
	</xsl:template>

	<xsl:template name="enrich">
		<xsl:if test="name(.)='graphml'">
			<key for="node" id="ynode" yfiles.type="nodegraphics" />
			<key for="edge" id="yedge" yfiles.type="edgegraphics" />
		</xsl:if>
		<xsl:if test="name(.)='node'">
			<data key="ynode">
				<xsl:element name="ShapeNode" namespace="http://www.yworks.com/xml/graphml">
					<xsl:element name="Fill" namespace="http://www.yworks.com/xml/graphml">
						<xsl:if test="graphml:data[@key='branched' and . = 'false']">
							<xsl:attribute name="color">#888888</xsl:attribute>
						</xsl:if>
						<xsl:if test="graphml:data[@key='noSolution' and . = 'true']">
                            <xsl:attribute name="color">#000000</xsl:attribute>
                        </xsl:if>
						<xsl:if test="graphml:data[@key='isGoalPath' and . = 'true']">
							<xsl:attribute name="color">#FF0000</xsl:attribute>
						</xsl:if>
						<xsl:attribute name="transparent">false</xsl:attribute>
					</xsl:element>
                    <xsl:element name="Shape" namespace="http://www.yworks.com/xml/graphml">
                        <xsl:attribute name="type">ellipse</xsl:attribute>
                    </xsl:element>
				</xsl:element>
			</data>
			<xsl:if test="graphml:data[@key='branched' and . = 'false']">
				<data key="ynode">
					<xsl:element name="ShapeNode" namespace="http://www.yworks.com/xml/graphml">
						<xsl:element name="Fill" namespace="http://www.yworks.com/xml/graphml">
							<xsl:attribute name="color">#FF0000</xsl:attribute>
							<xsl:attribute name="transparent">false</xsl:attribute>
						</xsl:element>
					</xsl:element>
				</data>
			</xsl:if>
		</xsl:if>
		<xsl:if test="name(.)='edge'">
			<data key="yedge">
				<y:BezierEdge xmlns:y="http://www.yworks.com/xml/graphml">
					<xsl:if test="graphml:data[@key='isPlanAction' and .= 'true']">
						<y:LineStyle color="#FF0000" width="1.0" />
					</xsl:if>
					<y:Arrows source="none" target="standard" />
					<xsl:element name="EdgeLabel" namespace="http://www.yworks.com/xml/graphml">
					   <xsl:if test="graphml:data[@key='isPlanAction' and .= 'true']">
					       <xsl:attribute name="textColor">#FF0000</xsl:attribute>
					   </xsl:if>
					   <xsl:text><xsl:value-of select="graphml:data[@key='action']" /></xsl:text>
					</xsl:element>
				</y:BezierEdge>
			</data>
		</xsl:if>
	</xsl:template>


</xsl:stylesheet>
  