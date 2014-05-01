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

package dk.dma.enav.safety;

import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Ellipse;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.util.CoordinateConverter;
import dk.dma.enav.util.geometry.Point;

import static java.lang.Math.max;

/**
 * This class holds static methods needed to calculate safety zones (ellipses) and elliptically approximated extents
 * for vessels.
 *
 * @author Thomas Borg Salling <tbsalling@tbsalling.dk>
 */
public final class SafetyZones {
    /**
     * Compute the an elliptic zone which roughly corresponds to the vessel's physical extent.
     *
     * @param position The reported position of the vessel.
     * @param cog Course over ground in compass degrees.
     * @param loa The vessel's length-overall (in meters).
     * @param beam The vessel's beam (in meters).
     * @param dimStern Distance from GPS antenna to vessel's stern (in meters).
     * @param dimStarboard Distance from GPS antenne to vessel's starboard beam (in meters).
     * @return an Ellipse approximately covering the vessel's extent.
     */
    public static Ellipse vesselExtent(Position position, float cog, float loa, float beam, float dimStern, float dimStarboard) {
        return vesselExtent(position, position, cog, loa, beam, dimStern, dimStarboard);
    }

    /**
     * Compute the an elliptic zone which roughly corresponds to the vessel's physical extent.
     *
     * The cartesian center is required to compare for intersection of the return ellipse when other ellipses. If no
     * such comparison is ever made, the alternative method with no geodetic center can be used.
     *
     * @param geodeticReference the geodetic reference needed to compare the returned Ellipse with other Ellipses.
     * @param position The reported position of the vessel.
     * @param cog Course over ground in compass degrees.
     * @param loa The vessel's length-overall (in meters).
     * @param beam The vessel's beam (in meters).
     * @param dimStern Distance from GPS antenna to vessel's stern (in meters).
     * @param dimStarboard Distance from GPS antenne to vessel's starboard beam (in meters).
     * @return an Ellipse approximately covering the vessel's extent.
     */
    public static Ellipse vesselExtent(Position geodeticReference, Position position, float cog, float loa, float beam, float dimStern, float dimStarboard) {
        return computeZone(geodeticReference, position, cog, loa, beam, dimStern, dimStarboard, 1.0, 1.0, 0.5);
    }

    /**
     * Compute the safety zone of track. This is roughly equivalent to the elliptic area around the vessel
     * which its navigator would observe for safety reasons to avoid imminent collisions.
     *
     * The cartesian center is required to compare for intersection of the return ellipse when other ellipses. If no
     * such comparison is ever made, the alternative method with no geodetic center can be used.
     *
     * @param geodeticReference the geodetic reference needed to compare the returned Ellipse with other Ellipses.
     * @param position The reported position of the vessel.
     * @param cog Course over ground in compass degrees.
     * @param sog Speed over ground in knots.
     * @param loa The vessel's length-overall (in meters).
     * @param beam The vessel's beam (in meters).
     * @param dimStern Distance from GPS antenna to vessel's stern (in meters).
     * @param dimStarboard Distance from GPS antenne to vessel's starboard beam (in meters).
     * @return an Ellipse approximately covering the vessel's extent.
     */
    public static Ellipse safetyZone(Position geodeticReference, Position position, float cog, float sog, float loa, float beam, float dimStern, float dimStarboard) {
        final double safetyEllipseLength = 4;
        final double safetyEllipseBreadth = 5;
        final double safetyEllipseBehind = 0.5;
        final double v = 1.0;  /* TODO should depend on sog */
        final double l1 = max(safetyEllipseLength*v, 1.0 + safetyEllipseBehind*v*2.0);
        final double b1 = max(safetyEllipseBreadth*v, 1.5);
        final double xc = -safetyEllipseBehind*v + 0.5*l1;
        return computeZone(geodeticReference, position, cog, loa, beam, dimStern, dimStarboard, l1, b1, xc);
    }

    /**
     * Compute an Ellipse around a track
     */
    private static Ellipse computeZone(Position geodeticReference, Position position, float cog, float loa, float beam, float dimStern, float dimStarboard, double l1, double b1, double xc) {
        // Compute direction of half axis alpha
        final double thetaDeg = CoordinateConverter.compass2cartesian(cog);

        // Transform latitude/longitude to cartesian coordinates
        final double centerLatitude = geodeticReference.getLatitude();
        final double centerLongitude = geodeticReference.getLongitude();
        final CoordinateConverter CoordinateConverter = new CoordinateConverter(centerLongitude, centerLatitude);

        final double trackLatitude = position.getLatitude();
        final double trackLongitude = position.getLongitude();
        final double x = CoordinateConverter.lon2x(trackLongitude, trackLatitude);
        final double y = CoordinateConverter.lat2y(trackLongitude, trackLatitude);

        // Compute center of safety zone
        final Point pt0 = new Point(x, y);
        Point pt1 = new Point(pt0.getX() - dimStern + loa*xc, pt0.getY() + dimStarboard - beam/2.0);
        pt1 = pt1.rotate(pt0, thetaDeg);

        // Compute length of half axis alpha
        final double alpha = loa*l1/2.0;

        // Compute length of half axis beta
        final double beta = beam*b1/2.0;

        return new Ellipse(geodeticReference, pt1.getX(), pt1.getY(), alpha, beta, thetaDeg, CoordinateSystem.CARTESIAN);
    }
}
