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

package net.maunium.recipebook.util;

/**
 * ErrorMessage is a simple error message intended for JSON serialization.
 *
 * This is intended to be returned from API endpoint handlers that use the
 * {@link JSON#transformer()} response transformer.
 *
 * @author Tulir Asokan
 */
public class ErrorMessage {
	/**
	 * The state of the app when this message is returned.
	 */
	public final String state = "error";
	/**
	 * A description of the error that occurred.
	 */
	public String message;

	public ErrorMessage(String message) {
		this.message = message;
	}
}
