/*
 *
 *  *
 *  *  *  Licensed to GIScience Research Group, Heidelberg University (GIScience)
 *  *  *
 *  *  *   http://www.giscience.uni-hd.de
 *  *  *   http://www.heigit.org
 *  *  *
 *  *  *  under one or more contributor license agreements. See the NOTICE file
 *  *  *  distributed with this work for additional information regarding copyright
 *  *  *  ownership. The GIScience licenses this file to you under the Apache License,
 *  *  *  Version 2.0 (the "License"); you may not use this file except in compliance
 *  *  *  with the License. You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *  Unless required by applicable law or agreed to in writing, software
 *  *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  See the License for the specific language governing permissions and
 *  *  *  limitations under the License.
 *  *
 *
 */

package org.heigit.ors.globalresponseprocessor;

import org.heigit.ors.common.StatusCode;
import org.heigit.ors.exceptions.ExportException;
import org.heigit.ors.exceptions.StatusCodeException;
import org.heigit.ors.globalresponseprocessor.geojson.GeoJsonResponseWriter;
import org.heigit.ors.globalresponseprocessor.gpx.GpxResponseWriter;
import org.heigit.ors.isochrones.IsochroneMapCollection;
import org.heigit.ors.isochrones.IsochroneRequest;
import org.heigit.ors.isochrones.IsochronesErrorCodes;
import org.heigit.ors.mapmatching.MapMatchingRequest;
import org.heigit.ors.matrix.MatrixErrorCodes;
import org.heigit.ors.matrix.MatrixRequest;
import org.heigit.ors.matrix.MatrixResult;
import org.heigit.ors.routing.RouteResult;
import org.heigit.ors.routing.RoutingErrorCodes;
import org.heigit.ors.routing.RoutingRequest;
import org.heigit.ors.services.ServiceRequest;
import org.heigit.ors.servlet.util.ServletUtility;
import org.json.JSONObject;

/**
 * The {@link GlobalResponseProcessor} works as a global Class to process all export functions in one place.
 * The class will assure, that the exports will mostly look the same or at least will reuse parts of it, so integrating new exports will result in minimal adjusting with already existing processions of ors exports.
 * The benefit is that the user can get an easy overview about existing export options and also easily modify existing and integrate new ones in one place.
 * <p>
 * The {@link GlobalResponseProcessor} doesn't include the {@link org.heigit.ors.servlet.util.ServletUtility} function to write the output.
 * So {@link org.heigit.ors.servlet.util.ServletUtility} must be called separately with the returned {@link JSONObject}.
 *
 * @author Julian Psotta, julian@openrouteservice.com
 * @deprecated
 */
@SuppressWarnings("FieldCanBeLocal")
@Deprecated
public class GlobalResponseProcessor {
    public static final String FORMAT_GEO_JSON = "GeoJSON";
    private IsochroneRequest isochroneRequest;
    private IsochroneMapCollection isochroneMapCollection; // The result type for Isochrones!!!
    private MapMatchingRequest mapMatchingRequest;
    private MatrixRequest matrixRequest;
    private MatrixResult matrixResult;
    private RoutingRequest routingRequest;
    private RouteResult[] routeResult;

    // TODO The constructors still need refinement with their inputs
    // TODO add try and catch errors to all subclasses
    // TODO finish commenting

    /**
     * Constructor to ensure the correct creation and processing of the desired export.
     *
     * @param isochroneRequest       {@link IsochroneRequest} holding the initial {@link ServiceRequest}.
     * @param isochroneMapCollection {@link IsochroneMapCollection} holding the already processed result.
     */
    public GlobalResponseProcessor(IsochroneRequest isochroneRequest, IsochroneMapCollection isochroneMapCollection) {

        this.isochroneRequest = isochroneRequest;
        this.isochroneMapCollection = isochroneMapCollection;
    }

    /**
     * Constructor to ensure the correct creation and processing of the desired export.
     *
     * @param mapMatchingRequest {@link MapMatchingRequest} holding the initial {@link ServiceRequest}.
     * @param routeResult        {@link RouteResult} holding the already processed result.
     */
    public GlobalResponseProcessor(MapMatchingRequest mapMatchingRequest, RouteResult[] routeResult) {

        this.mapMatchingRequest = mapMatchingRequest;
        this.routeResult = routeResult;
    }

    /**
     * Constructor to ensure the correct creation and processing of the desired export.
     *
     * @param matrixRequest {@link MatrixRequest} holding the initial {@link ServiceRequest}.
     * @param matrixResult  {@link MatrixResult} holding the already processed result.
     */
    public GlobalResponseProcessor(MatrixRequest matrixRequest, MatrixResult matrixResult) {

        this.matrixRequest = matrixRequest;
        this.matrixResult = matrixResult;
    }

    /**
     * Constructor to ensure the correct creation and processing of the desired export.
     *
     * @param request {@link RoutingRequest} holding the initial {@link ServiceRequest}.
     * @param result  {@link RouteResult} holding the already processed result.
     */
    public GlobalResponseProcessor(RoutingRequest request, RouteResult[] result) {
        this.routingRequest = request;
        this.routeResult = result;
    }

    /**
     * The function works as a distribution class that is/will be able to process any kind of request result combination as an input.
     * If the function doesn't provide a specific Export for a specific {@link ServiceRequest} yet, the {@link JSONObject} will be returned empty.
     *
     * @return The method returns a GeoJson as a {@link JSONObject} that can be directly imported into {@link ServletUtility}'s write function. If a specific {@link ServiceRequest} isn't integrated yet, the {@link JSONObject} will be empty.
     * @throws Exception An error will be raised using {@link ExportException}.
     */
    public JSONObject toGeoJson() throws Exception {
        // Check for the correct ServiceRequest and chose the right export function
        // TODO Integrate all exports here by time
        if (this.isochroneRequest != null) {
            throw new ExportException(IsochronesErrorCodes.UNSUPPORTED_EXPORT_FORMAT, this.getClass(), isochroneRequest.getClass(), FORMAT_GEO_JSON);
//            if (this.isochroneMapCollection.size() > 0) {
//                // TODO Do export
//            }
        } else if (this.mapMatchingRequest != null) {
            throw new StatusCodeException(StatusCode.NOT_IMPLEMENTED);
//            if (this.isochroneMapCollection.size() > 0) {
//                // TODO Do export
//            }
        } else if (this.matrixRequest != null) {
            throw new ExportException(MatrixErrorCodes.UNSUPPORTED_EXPORT_FORMAT, this.getClass(), matrixRequest.getClass(), FORMAT_GEO_JSON);
//            if (this.matrixResult.getSources().length > 0 && this.matrixResult.getDestinations().length > 0) {
//                // TODO Do export
//            }
        } else if (this.routingRequest != null) {
            try {
                if (this.routeResult.length > 0)
                    return GeoJsonResponseWriter.toGeoJson(routingRequest, routeResult);
            } catch (ExportException e) {
                throw new ExportException(RoutingErrorCodes.EXPORT_HANDLER_ERROR, this.routingRequest.getClass(), FORMAT_GEO_JSON);
            }
        }
        return null;
    }

    /**
     * The function works as a distribution class that is/will be able to process any kind of request result combination as an input.
     * If the function doesn't provide a specific Export for a specific {@link ServiceRequest} yet, the {@link JSONObject} will be returned empty.
     *
     * @return The method returns a GPX as a {@link String} that can be directly imported into {@link ServletUtility}'s write function. If a specific {@link ServiceRequest} isn't integrated yet, the {@link String} will be empty.
     * @throws Exception An error will be raised using {@link ExportException}.
     */
    public String toGPX() throws Exception {
        // Check for the correct ServiceRequest and chose the right export function
        // TODO Integrate all exports here by time
        if (this.isochroneRequest == null) {
            if (this.mapMatchingRequest != null) {
                throw new StatusCodeException(StatusCode.NOT_IMPLEMENTED);
            } else if (this.matrixRequest != null) {
                throw new ExportException(MatrixErrorCodes.UNSUPPORTED_EXPORT_FORMAT, this.getClass(), matrixRequest.getClass(), "GPX");
                /*if (this.matrixResult.getSources().length > 0 && this.matrixResult.getDestinations().length > 0) {
                    // TODO Do export
                }*/
            } else if (this.routingRequest != null) {
                try {
                    if (this.routeResult.length > 0)
                        return GpxResponseWriter.toGPX(routingRequest, routeResult);
                } catch (ExportException e) {
                    throw new ExportException(RoutingErrorCodes.EXPORT_HANDLER_ERROR, this.routingRequest.getClass(), "GPX");
                }
            }
        } else {
            throw new ExportException(IsochronesErrorCodes.UNSUPPORTED_EXPORT_FORMAT, this.getClass(), isochroneRequest.getClass(), "GPX");
            /*if (this.isochroneMapCollection.size() > 0) {
                // TODO Do export
            }*/
        }
        return null;
    }

}
