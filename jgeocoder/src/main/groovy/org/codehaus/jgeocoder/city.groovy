package org.codehaus.jgeocoder

PrintStream ps = new PrintStream('city_dict')
def all = [:]
new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream('org/codehaus/jgeocoder/index/ZIP_CODES.txt')).eachLine{
	def values = it.split(/\s*,\s*/)
	def zip = values[0].replaceAll("\"", "");
	def city = values[3].replaceAll("\"", "");
	def county = values[5].replaceAll("\"", "");
	
	def cityTokens = city.split(/\s+/)
	
	cityTokens.each{ct ->
		if (ct.length() <= 3 ){
			return;
		}
		
		def line = all[ct]
		if(line == null){
			all[ct] = zip
		}else{
			all[ct] = line+","+zip
		}
	}
	
}

all.each{k, v ->
	ps.println "$k|$v"
}