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
	public void initObjectContained(String ShelveType) { 	//Les étales initialisent les objets qu'elles contiennent.
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
		else if (ShelveType == "House2") {
			objectContained.add(new ImageDraw("house2"));
		}
		else if (ShelveType == "Flower") {
			for (int i = 0; i<8; i++) {
				objectContained.add(new Flower());
			}
		}
	}
	public String getShelveType() {
		return shelveType;
	}
	@Override
	public void makeSprite() {
		sprite = Constantes.étagère;
	}
}