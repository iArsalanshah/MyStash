/**
 * Copyright 2014 Joan Zapata
 * <p>
 * This file is part of Android-pdfview.
 * <p>
 * Android-pdfview is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Android-pdfview is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Android-pdfview.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.joanzapata.pdfview.listener;

/**
 * Implements this interface to receive events from IPDFView
 * when loading is compete.
 */
public interface OnLoadCompleteListener {

    /**
     * Called when the PDF is loaded
     * @param nbPages the number of pages in this PDF file
     */
    void loadComplete(int nbPages);
}
