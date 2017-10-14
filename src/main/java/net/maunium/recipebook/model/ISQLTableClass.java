package net.maunium.recipebook.model;

/**
 * ISQLTableClass is an interface that is implemented by all data classes that
 * are directly related to a non-linking database table.
 *
 * @author Tulir Asokan
 * @project RecipeBook
 */
public interface ISQLTableClass {
	void update();
	void insert();
	void delete();
	// static T get(int id);
}
