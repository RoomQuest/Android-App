public Wifi parseWifiFile(String file) {
		try {
			return parseWifiFile(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Wifi parseWifiFile(InputStream input) {
		BufferedReader br = null;
		String line = "";
		String splitBy = ",";
		
		Wifi wifi = new Wifi();
		List<Hotspot> hotspotList = new ArrayList<Hotspot>();
		
		try {
			br = new BufferedReader(new InputStreamReader(input));
			while((line = br.readLine()) != null) {
				
				String [] hotspots = line.split(splitBy);
				
				
				Hotspot hotspotObject = new Hotspot();
				
				
				hotspotObject.setXLocation(Integer.parseInt(hotspots[0]));
				hotspotObject.setYLocation(Integer.parseInt(hotspots[1]));
				hotspotObject.setBSSID(hotspots[2]);
				hotspotObject.setPower(hotspots[3]);
				hotspotList.add(hotspotObject);
				
			}
		} catch(Exception e) {
			e.printStackTrace();
        }finally {
			if(br != null) {
				try {
					br.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}

        return wifi;
	}
}
