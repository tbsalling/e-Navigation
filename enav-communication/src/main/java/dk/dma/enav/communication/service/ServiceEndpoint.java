/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.enav.communication.service;

import dk.dma.enav.communication.NetworkFuture;
import dk.dma.enav.model.MaritimeId;

/**
 * 
 * @author Kasper Nielsen
 */
public interface ServiceEndpoint<E, T> {

    /**
     * Returns the id of the end point
     * 
     * @return the id of the end point
     */
    MaritimeId getId();

    NetworkFuture<T> invoke(E message);
}