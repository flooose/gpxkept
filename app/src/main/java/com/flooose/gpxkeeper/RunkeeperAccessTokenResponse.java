/*
    Copyright 2015-2080 christopher floess

    This file is part of gpxkept.

    gpxkept is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    gpxkept is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with gpxkept.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.flooose.gpxkeeper;

import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.token.OAuthToken;

/**
 * Created by chris on 26.04.15.
 */
public class RunkeeperAccessTokenResponse extends OAuthAccessTokenResponse {
    @Override
    public String getAccessToken() {
        return null;
    }

    @Override
    public Long getExpiresIn() {
        return null;
    }

    @Override
    public String getRefreshToken() {
        return null;
    }

    @Override
    public String getScope() {
        return null;
    }

    @Override
    public OAuthToken getOAuthToken() {
        return null;
    }

    @Override
    protected void setBody(String body) throws OAuthProblemException {

    }

    @Override
    protected void setContentType(String contentType) {

    }

    @Override
    protected void setResponseCode(int responseCode) {

    }
}
