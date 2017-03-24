package model;

public interface Observable<E> {
	public void register(Observer<E> o);
	public void unregister(Observer<E> o);
	public void notifyObservers();
}
