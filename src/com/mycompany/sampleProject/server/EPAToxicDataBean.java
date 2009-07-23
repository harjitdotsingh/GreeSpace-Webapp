package com.mycompany.sampleProject.server;

import javax.jdo.annotations.IdGeneratorStrategy; 
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true") 
public class EPAToxicDataBean  implements IsSerializable{ 

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	private String FacilityregistryId;
	@Persistent
	private String FacilitySiteName;
	@Persistent
	private String LocationAddressText;
	@Persistent
	private String LocalityName;
	@Persistent
	private String LocationAddressStateCode;  
	@Persistent
	public String LocationZIPCode; 
	
	@Persistent
	private double LatitudeMeasure;  
	@Persistent
	private double LongitudeMeasure;
	private String HorizontalCoordinateReferenceSystemDatumName;
	private String HorizontalCollectionMethodName;

	private String ElectronicAddressText;
	private String ElectronicAddressTypeName;
	@Persistent
	private String ProgramCommonName;
	@Persistent
	private String ProgramAcronymName;
	@Persistent
	private String ProgramIdentifier;
	@Persistent
	private String ProgramInterestType;

	private String ProgramFullName;

	public String getProgramFullName() {
		return ProgramFullName;
	}

	public void setProgramFullName(String programFullName) {
		ProgramFullName = programFullName;
	}

	public void setProgramInterestType(String programInterestType) { 
		ProgramInterestType = programInterestType;
	}

	@Persistent
	private String ProgramFacilitySiteName;

	public String getProgramFacilitySiteName() {
		return ProgramFacilitySiteName;
	}

	public void setProgramFacilitySiteName(String programFacilitySiteName) {
		ProgramFacilitySiteName = programFacilitySiteName;
	}

	public String getFacilityregistryId() {
		return FacilityregistryId;
	}

	public void setFacilityregistryId(String facilityregistryId) {
		FacilityregistryId = facilityregistryId;
	}

	public String getFacilitySiteName() {
		return FacilitySiteName;
	}

	public void setFacilitySiteName(String facilitySiteName) {
		FacilitySiteName = facilitySiteName;
	}

	public String getLocationAddressText() {
		return LocationAddressText;
	}

	public void setLocationAddressText(String locationAddressText) {
		LocationAddressText = locationAddressText;
	}

	public String getLocalityName() {
		return LocalityName;
	}

	public void setLocalityName(String localityName) {
		LocalityName = localityName;
	}

	public String getLocationAddressStateCode() {
		return LocationAddressStateCode;
	}

	public void setLocationAddressStateCode(String locationAddressStateCode) {
		LocationAddressStateCode = locationAddressStateCode;
	}

	public String getLocationZIPCode() {
		return LocationZIPCode;
	}

	public void setLocationZIPCode(String locationZIPCode) {
		LocationZIPCode = locationZIPCode;
	}

	public double getLatitudeMeasure() {
		return LatitudeMeasure;
	}

	public void setLatitudeMeasure(double latitudeMeasure) {
		LatitudeMeasure = latitudeMeasure;
	}

	public double getLongitudeMeasure() {
		return LongitudeMeasure;
	}

	public void setLongitudeMeasure(double longitudeMeasure) {
		LongitudeMeasure = longitudeMeasure;
	}

	public String getHorizontalCoordinateReferenceSystemDatumName() {
		return HorizontalCoordinateReferenceSystemDatumName;
	}

	public void setHorizontalCoordinateReferenceSystemDatumName(
			String horizontalCoordinateReferenceSystemDatumName) {
		HorizontalCoordinateReferenceSystemDatumName = horizontalCoordinateReferenceSystemDatumName;
	}

	public String getHorizontalCollectionMethodName() {
		return HorizontalCollectionMethodName;
	}

	public void setHorizontalCollectionMethodName(
			String horizontalCollectionMethodName) {
		HorizontalCollectionMethodName = horizontalCollectionMethodName;
	}

	public String getElectronicAddressText() {
		return ElectronicAddressText;
	}

	public void setElectronicAddressText(String electronicAddressText) {
		ElectronicAddressText = electronicAddressText;
	}

	public String getElectronicAddressTypeName() {
		return ElectronicAddressTypeName;
	}

	public void setElectronicAddressTypeName(String electronicAddressTypeName) {
		ElectronicAddressTypeName = electronicAddressTypeName;
	}

	public String getProgramCommonName() {
		return ProgramCommonName;
	}

	public void setProgramCommonName(String programCommonName) {
		ProgramCommonName = programCommonName;
	}

	public String getProgramAcronymName() {
		return ProgramAcronymName;
	}

	public void setProgramAcronymName(String programAcronymName) {
		ProgramAcronymName = programAcronymName;
	}

	public String getProgramIdentifier() {
		return ProgramIdentifier;
	}

	public void setProgramIdentifier(String programIdentifier) {
		ProgramIdentifier = programIdentifier;
	}

	public String getProgramInterestType() {
		return ProgramInterestType;
	}

	public void setData(String[] data) {
		System.out.println("Site Registration:" + data[0]);
		FacilityregistryId = data[0];
		if (data.length >=2) {
			FacilitySiteName = data[1].trim(); 
		}
		if (data.length >=3) {
			LocationAddressText = data[2].trim();
		}
		if (data.length >=4) {
			LocalityName = data[3].trim();
		}
		if (data.length >=5) {
			LocationAddressStateCode = data[4].trim();
		}
		if (data.length >=6) {
			LocationZIPCode = data[5].trim();
		}
		if (data.length >=7) {
			LatitudeMeasure = new Double(data[6].trim());
		}
		if (data.length >=8) {
			LongitudeMeasure = new Double(data[7].trim());
		}
		if (data.length >=9) {
			HorizontalCoordinateReferenceSystemDatumName = data[8];
		}
		if (data.length >=10) {
			HorizontalCollectionMethodName = data[9];
		}
		if (data.length >=11) {
			ElectronicAddressText = data[10];
		}

		if (data.length >=12) {
			ElectronicAddressTypeName = data[11];
		}
		if (data.length >=13) {
			ProgramCommonName = data[12];
		}
		if (data.length >=14) {
			ProgramAcronymName = data[13];
		}
		if (data.length >=15) {
			ProgramIdentifier = data[14];
		}
		if (data.length >=16) {
			ProgramInterestType = data[15];
		}
		if (data.length >=17) {
			ProgramFullName = data[16];
		}
		if (data.length == 18) {
			ProgramFacilitySiteName = data[17];
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
