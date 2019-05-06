package model;

public class MarketShelve extends ContainerObject{
	private String shelveType ;
	public MarketShelve(int x, int y, String shelveType) {
		super(x,y);
		this.shelveType = shelveType;
		initObjectContained(shelveType);
		
	}
	public MarketShelve() {
		super();
	}
	public void initObjectContained(String ShelveType) {
		if (objectContained.size() != 0) {
			objectContained.clear();
		}
		if (ShelveType == "Apple") {
			for (int i = 0; i<8; i++) {
				objectContained.add(new Apple());
			}
		}
		else if (ShelveType == "Cigaret") {
			for (int i = 0; i<8; i++) {
				objectContained.add(new Cigaret());
			}
		}
	}
	public String getShelveType() {
		return shelveType;
	}
	public void makeSprite() {
		sprite = Constantes.étagère;
	}
}