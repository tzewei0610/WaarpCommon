/**
   This file is part of GoldenGate Project (named also GoldenGate or GG).

   Copyright 2009, Frederic Bregier, and individual contributors by the @author
   tags. See the COPYRIGHT.txt in the distribution for a full listing of
   individual contributors.

   All GoldenGate Project is free software: you can redistribute it and/or 
   modify it under the terms of the GNU General Public License as published 
   by the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   GoldenGate is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with GoldenGate .  If not, see <http://www.gnu.org/licenses/>.
 */
package goldengate.common.crypto;

/**
 * This class implements a simple Key Manager for DES class from name
 *
 * @author frederic bregier
 */
public class DesManager extends KeyManager {
    public static final DesManager desManager = new DesManager();


    /* (non-Javadoc)
     * @see atlas.cryptage.KeyManager#createKeyObject()
     */
    @Override
    public KeyObject createKeyObject() {
        return new Des();
    }
}