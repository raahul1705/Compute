/*
Copyright (c) 2011, Sony Mobile Communications Inc.
Copyright (c) 2014, Sony Corporation

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 * Neither the name of the Sony Mobile Communications Inc.
 nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.rxn.compute;

/**
 * Provides constants.
 */
public final class Constants {

    // Extension key
    public static final String EXTENSION_KEY = Constants.class.getPackage().getName() + ".key";

    // Log tags
    //public static final String LOG_TAG = "CameraNavigationExtension";
    public static final String LOG_TAG = "CameraNavExtension";
    public static final String IMAGE_MANAGER_TAG = "ImageManager";
    public static final String IMAGE_RESULT_ACTIVITY_TAG = "ImageResultActivity";
    public static final String PROCESS_IMAGE_RUNNABLE_TAG = "ProcessImageRunnable";
    public static final String CLIENT_SOCKET_THREAD_TAG = "ClientSocketThread";
    public static final String NETWORK_MANAGER_TAG = "NetworkManager";
    public static final String LOAD_MANAGER_TAG = "LoadManager";

    // Message status tags for use by handlers to do object detection on mobile device
    public static final int IMAGE_PROCESSING_FAILED = 0;
    public static final int IMAGE_PROCESSING_COMPLETED = 1;
    public static final int STREAMED_IMAGE_READY = 2;
    public static final int BOUNDING_BOXES_READY = 3;
    public static final int CLEAR_BOUNDING_BOXES = 4;
    public static final int REQUEST_FOR_IMAGE_VIEW_REFERENCE = 5;
    public static final int IMAGE_VIEW_REFERENCE_READY = 6;

    // Message tags used by handlers to start, stop, and update beeps
    public static final int BEEP_FREQUENCY_CLEAR = 7;
    public static final int BEEP_FREQUENCY_CAREFUL = 8;
    public static final int BEEP_FREQUENCY_DANGEROUS = 9;
    public static final int PLAY_BEEP_SOUND = 10;
    public static final int STOP_BEEPS = 11;

    // Message tags used by handlers to send images to server and check server availability
    public static final int STREAMED_IMAGE_READY_FOR_SERVER = 12;
    public static final int SERVER_AVAILABLE = 13;
    public static final int SERVER_UNAVAILABLE = 14;
    public static final int SERVER_STARTED_RESPONSE_RECEIVED = 15;

    /** Hides the default constructor. */
    private Constants() {
    }
}
