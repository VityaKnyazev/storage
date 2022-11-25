package by.itacademy.javaenterprise.knyazev.utils;

public enum Unit {
	кг(0L), г, т, шт, ед(0L);
	
	private Long collect;
	Unit() {}
	
	Unit(Long collect) {
		this.collect = collect;
	}
	
	public Long getCollect() {
		return collect;
	}
}