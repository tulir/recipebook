// RecipeBook - An Introduction to Databases exercise project with Java Spark and React.
// Copyright (C) 2017  Maunium

// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

package net.maunium.recipebook.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ISQLTableClass is an interface that is implemented by all data classes that
 * are directly related to a non-linking database table.
 *
 * @author Tulir Asokan
 */
public interface ISQLTableClass {
	/**
	 * Update the database row this object represents with the new data in this object.
	 */
	void update();

	/**
	 * Insert this object into the database as a new row.
	 */
	void insert();

	/**
	 * Delete the database row this object represents.
	 */
	void delete();

	// This method is expected to be implemented as well, but interfaces can't require static methods.
	// static T get(int id);

	/**
	 * Utility function to run {@link PreparedStatement#executeUpdate()} and get the ID of the insert.
	 * @param stmt The PreparedStatement to execute.
	 * @return The ID of the inserted row, or 0 if something went wrong.
	 * @throws SQLException If a PreparedStatement method call throws something.
	 *                      See {@link PreparedStatement#executeUpdate()},
	 *                      {@link PreparedStatement#getGeneratedKeys()} and
	 *                      {@link ResultSet#getInt(int)}
 	 */
	public static int insertAndGetID(PreparedStatement stmt) throws SQLException {
		stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
			return rs.getInt(1);
		}
		return 0;
	}
}
