package xyz.telosaddon.yuno.gson;

import com.google.gson.annotations.SerializedName;

public class Values{

	@SerializedName("SPEED")
	private int sPEED;

	@SerializedName("DEFENSE")
	private int dEFENSE;

	@SerializedName("CRITCHANCE")
	private int cRITCHANCE;

	@SerializedName("VITALITY")
	private int vITALITY;

	@SerializedName("ATTACK")
	private int aTTACK;

	@SerializedName("HEALTH")
	private int hEALTH;

	@SerializedName("EVASION")
	private int eVASION;

	public int getSPEED(){
		return sPEED;
	}

	public int getDEFENSE(){
		return dEFENSE;
	}

	public int getCRITCHANCE(){
		return cRITCHANCE;
	}

	public int getVITALITY(){
		return vITALITY;
	}

	public int getATTACK(){
		return aTTACK;
	}

	public int getHEALTH(){
		return hEALTH;
	}

	public int getEVASION(){
		return eVASION;
	}
}