package Utils

import com.utils.CustomEdge
import com.utils.LinkBase
import com.utils.NodeBase
import com.utils.NodeType
import java.util.HashMap;
import SMOH.HBase;


class Helper {
	def private static GenerateEdgeList(List<NodeBase> lstNodeBase, HashMap<String, HBase> varList) {
		List<CustomEdge> lstLink = new ArrayList<CustomEdge>();
		
		varList.each {
			
			def isConnected = it.value.getProperties().find() { key, value -> 
				if(((String)key).contains("connected")) {
					return value
				}
				else{
					return false
				}
			}
			
			def linkLength = it.value.getLinkLength()
			
			def objName = it.value.getProperties().find() { key, value ->
				if(((String)key).contains("caption") && isConnected != null && isConnected.value) {
					return value
				}
			}
			
			def startID = lstNodeBase.find(){
				if(isConnected != null && it.name == isConnected.value){
					println "startID = " + it.id		
					return it.id
				}
			}
			
			def stopId = lstNodeBase.find(){
				if(objName != null && it.name == objName.value){
					println "stopID = " + it.id
					return it.id
				}
			} 
			
			def linkDesc = lstNodeBase.find(){
					if(stopId != null && it.id == stopId.id){
						return it.connLabel
				}
			}
			
			if (isConnected && objName && startID && stopId){
				lstLink.add(new CustomEdge(new LinkBase(0,0), startID, stopId, "DIRECTED", linkDesc.connLabel, (Integer)linkLength))
			}
		}
			
		return lstLink;
	}

	def private static GenerateVertexList (HashMap<String, HBase> varList) {
		List<NodeBase> lstBase = new ArrayList<NodeBase>();
		Integer counter = 0;
		
		varList.each {
			counter++;
			
			def node = (NodeType)it.value.type
			def connSpeed = it.value.getConnectionSpeed()
			
			it.value.getProperties().find()  { key, value ->
				if(((String)key).contains("caption")) {
					//println "wartosc" + value
					//println "counter" + counter
					
					lstBase.add(new NodeBase(counter, (String)value, node, (String)connSpeed))
				}
			}
		}
		return lstBase;
	}
}
