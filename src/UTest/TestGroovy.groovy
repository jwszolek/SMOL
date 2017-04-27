package UTest

import SMOH.*
import com.sample.DrawFirst

println "testowy napis"

DrawFirst df = new DrawFirst()
HashMap<HBase> varList = new HashMap();

//define central node  
def(root) = [new MainNode("ROOT")]
varList.put("root",root);

def sensorNames=["wifi10","wifi11","wifi12","wifi13", "wifi14"]
def sensorNames2=["wifi15","wifi16","wifi17","wifi19"]
