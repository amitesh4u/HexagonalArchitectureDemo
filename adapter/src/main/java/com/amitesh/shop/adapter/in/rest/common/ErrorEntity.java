package com.amitesh.shop.adapter.in.rest.common;

/**
 * An error entity with a status and message returned via REST API in case of an error.
 * @param httpStatus HTTP status
 * @param errorMessage Error message
 */
public record ErrorEntity(int httpStatus, String errorMessage) {

}
