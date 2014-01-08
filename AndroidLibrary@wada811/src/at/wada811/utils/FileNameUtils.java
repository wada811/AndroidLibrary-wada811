/*
 * Copyright 2014 wada811<at.wada811@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.wada811.utils;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;

/**
 * General filename and filepath manipulation utilities.
 * <p>
 * When dealing with filenames you can hit problems when moving from a Windows based development
 * machine to a Unix based production machine. This class aims to help avoid those problems.
 * <p>
 * <b>NOTE</b>: You may be able to avoid using this class entirely simply by using JDK
 * {@link java.io.File File} objects and the two argument constructor
 * {@link java.io.File#File(java.io.File, java.lang.String) File(File,String)}.
 * <p>
 * Most methods on this class are designed to work the same on both Unix and Windows. Those that
 * don't include 'System', 'Unix' or 'Windows' in their name.
 * <p>
 * Most methods recognise both separators (forward and back), and both sets of prefixes. See the
 * javadoc of each method for details.
 * <p>
 * This class defines six components within a filename (example C:\dev\project\file.txt):
 * <ul>
 * <li>the prefix - C:\</li>
 * <li>the path - dev\project\</li>
 * <li>the full path - C:\dev\project\</li>
 * <li>the name - file.txt</li>
 * <li>the base name - file</li>
 * <li>the extension - txt</li>
 * </ul>
 * Note that this class works best if directory filenames end with a separator. If you omit the last
 * separator, it is impossible to determine if the filename corresponds to a file or a directory. As
 * a result, we have chosen to say it corresponds to a file.
 * <p>
 * This class only supports Unix and Windows style names. Prefixes are matched as follows:
 * 
 * <pre>
 * Windows:
 * a\b\c.txt           --> ""          --> relative
 * \a\b\c.txt          --> "\"         --> current drive absolute
 * C:a\b\c.txt         --> "C:"        --> drive relative
 * C:\a\b\c.txt        --> "C:\"       --> absolute
 * \\server\a\b\c.txt  --> "\\server\" --> UNC
 * 
 * Unix:
 * a/b/c.txt           --> ""          --> relative
 * /a/b/c.txt          --> "/"         --> absolute
 * ~/a/b/c.txt         --> "~/"        --> current user
 * ~                   --> "~/"        --> current user (slash added)
 * ~user/a/b/c.txt     --> "~user/"    --> named user
 * ~user               --> "~user/"    --> named user (slash added)
 * </pre>
 * 
 * Both prefix styles are matched always, irrespective of the machine that you are currently running
 * on.
 * <p>
 * Origin of code: Excalibur, Alexandria, Tomcat, Commons-Utils.
 * 
 * @version $Id: FilenameUtils.java 1307462 2012-03-30 15:13:11Z ggregory $
 * @since 1.1
 */
public class FileNameUtils {

    /**
     * The extension separator character.
     * 
     * @since 1.4
     */
    public static final char EXTENSION_SEPARATOR = '.';

    /**
     * The extension separator String.
     * 
     * @since 1.4
     */
    public static final String EXTENSION_SEPARATOR_STR = Character.toString(EXTENSION_SEPARATOR);

    /**
     * The system separator character.
     */
    private static final char SYSTEM_SEPARATOR = File.separatorChar;

    /**
     * Instances should NOT be constructed in standard programming.
     */
    private FileNameUtils() {
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if the character is a separator.
     * 
     * @param ch the character to check
     * @return true if it is a separator character
     */
    private static boolean isSeparator(char ch){
        return ch == SYSTEM_SEPARATOR;
    }

    //-----------------------------------------------------------------------
    /**
     * Normalizes a path, removing double and single dot path steps.
     * <p>
     * This method normalizes a path to a standard format. The input may contain separators in
     * either Unix or Windows format. The output will contain separators in the format of the
     * system.
     * <p>
     * A trailing slash will be retained. A double slash will be merged to a single slash (but UNC
     * names are handled). A single dot path segment will be removed. A double dot will cause that
     * path segment and the one before to be removed. If the double dot has no parent path segment
     * to work with, {@code null} is returned.
     * <p>
     * The output will be the same on both Unix and Windows except for the separator character.
     * 
     * <pre>
     * /foo//               -->   /foo/
     * /foo/./              -->   /foo/
     * /foo/../bar          -->   /bar
     * /foo/../bar/         -->   /bar/
     * /foo/../bar/../baz   -->   /baz
     * //foo//./bar         -->   /foo/bar
     * /../                 -->   null
     * ../foo               -->   null
     * foo/bar/..           -->   foo/
     * foo/../../bar        -->   null
     * foo/../bar           -->   bar
     * //server/foo/../bar  -->   //server/bar
     * //server/../bar      -->   null
     * C:\foo\..\bar        -->   C:\bar
     * C:\..\bar            -->   null
     * ~/foo/../bar/        -->   ~/bar/
     * ~/../bar             -->   null
     * </pre>
     * 
     * (Note the file separator returned will be correct for Windows/Unix)
     * 
     * @param filename the filename to normalize, null returns null
     * @return the normalized filename, or null if invalid
     */
    public static String normalize(String filename){
        if(filename == null){
            return null;
        }
        int size = filename.length();
        if(size == 0){
            return filename;
        }
        int prefix = getPrefixLength(filename);
        if(prefix < 0){
            return null;
        }

        char[] array = new char[size + 2]; // +1 for possible extra slash, +2 for arraycopy
        filename.getChars(0, filename.length(), array, 0);

        // add extra separator on the end to simplify code below
        boolean lastIsDirectory = true;
        if(array[size - 1] != SYSTEM_SEPARATOR){
            array[size++] = SYSTEM_SEPARATOR;
            lastIsDirectory = false;
        }

        // adjoining slashes
        for(int i = prefix + 1; i < size; i++){
            if(array[i] == SYSTEM_SEPARATOR && array[i - 1] == SYSTEM_SEPARATOR){
                System.arraycopy(array, i, array, i - 1, size - i);
                size--;
                i--;
            }
        }

        // dot slash
        for(int i = prefix + 1; i < size; i++){
            if(array[i] == SYSTEM_SEPARATOR && array[i - 1] == '.' && (i == prefix + 1 || array[i - 2] == SYSTEM_SEPARATOR)){
                if(i == size - 1){
                    lastIsDirectory = true;
                }
                System.arraycopy(array, i + 1, array, i - 1, size - i);
                size -= 2;
                i--;
            }
        }

        // double dot slash
        outer:
        for(int i = prefix + 2; i < size; i++){
            if(array[i] == SYSTEM_SEPARATOR && array[i - 1] == '.' && array[i - 2] == '.' && (i == prefix + 2 || array[i - 3] == SYSTEM_SEPARATOR)){
                if(i == prefix + 2){
                    return null;
                }
                if(i == size - 1){
                    lastIsDirectory = true;
                }
                int j;
                for(j = i - 4; j >= prefix; j--){
                    if(array[j] == SYSTEM_SEPARATOR){
                        // remove b/../ from a/b/../c
                        System.arraycopy(array, i + 1, array, j + 1, size - i);
                        size -= i - j;
                        i = j + 1;
                        continue outer;
                    }
                }
                // remove a/../ from a/../c
                System.arraycopy(array, i + 1, array, prefix, size - i);
                size -= i + 1 - prefix;
                i = prefix + 1;
            }
        }

        if(size <= 0){ // should never be less than 0
            return "";
        }
        if(size <= prefix){ // should never be less than prefix
            return new String(array, 0, size);
        }
        if(lastIsDirectory){
            return new String(array, 0, size); // keep trailing separator
        }
        return new String(array, 0, size - 1); // lose trailing separator
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the length of the filename prefix, such as <code>C:/</code> or <code>~/</code>.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * <p>
     * The prefix length includes the first slash in the full filename if applicable. Thus, it is
     * possible that the length returned is greater than the length of the input string.
     * 
     * <pre>
     * Windows:
     * a\b\c.txt           --> ""          --> relative
     * \a\b\c.txt          --> "\"         --> current drive absolute
     * C:a\b\c.txt         --> "C:"        --> drive relative
     * C:\a\b\c.txt        --> "C:\"       --> absolute
     * \\server\a\b\c.txt  --> "\\server\" --> UNC
     * 
     * Unix:
     * a/b/c.txt           --> ""          --> relative
     * /a/b/c.txt          --> "/"         --> absolute
     * ~/a/b/c.txt         --> "~/"        --> current user
     * ~                   --> "~/"        --> current user (slash added)
     * ~user/a/b/c.txt     --> "~user/"    --> named user
     * ~user               --> "~user/"    --> named user (slash added)
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on. ie. both
     * Unix and Windows prefixes are matched regardless.
     * 
     * @param filename the filename to find the prefix in, null returns -1
     * @return the length of the prefix, -1 if invalid or null
     */
    private static int getPrefixLength(String filename){
        if(filename == null){
            return -1;
        }
        int len = filename.length();
        if(len == 0){
            return 0;
        }
        char ch0 = filename.charAt(0);
        if(ch0 == ':'){
            return -1;
        }
        if(len == 1){
            if(ch0 == '~'){
                return 2; // return a length greater than the input
            }
            return isSeparator(ch0) ? 1 : 0;
        }else{
            if(ch0 == '~'){
                int posUnix = filename.indexOf(SYSTEM_SEPARATOR, 1);
                if(posUnix == -1){
                    return len + 1; // return a length greater than the input
                }
                return posUnix + 1;
            }
            char ch1 = filename.charAt(1);
            if(ch1 == ':'){
                ch0 = Character.toUpperCase(ch0);
                if(ch0 >= 'A' && ch0 <= 'Z'){
                    if(len == 2 || isSeparator(filename.charAt(2)) == false){
                        return 2;
                    }
                    return 3;
                }
                return -1;

            }else if(isSeparator(ch0) && isSeparator(ch1)){
                int posUnix = filename.indexOf(SYSTEM_SEPARATOR, 2);
                if(posUnix == -1 || posUnix == 2){
                    return -1;
                }
                return posUnix + 1;
            }else{
                return isSeparator(ch0) ? 1 : 0;
            }
        }
    }

    /**
     * Returns the index of the last directory separator character.
     * <p>
     * This method will handle a file in either Unix or Windows format. The position of the last
     * forward or backslash is returned.
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * 
     * @param filename the filename to find the last path separator in, null returns -1
     * @return the index of the last separator character, or -1 if there
     *         is no such character
     */
    private static int indexOfLastSeparator(String filename){
        if(filename == null){
            return -1;
        }
        int lastUnixPos = filename.lastIndexOf(SYSTEM_SEPARATOR);
        return lastUnixPos;
    }

    /**
     * Returns the index of the last extension separator character, which is a dot.
     * <p>
     * This method also checks that there is no directory separator after the last dot. To do this
     * it uses {@link #indexOfLastSeparator(String)} which will handle a file in either Unix or
     * Windows format.
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * 
     * @param filename the filename to find the last path separator in, null returns -1
     * @return the index of the last separator character, or -1 if there
     *         is no such character
     */
    private static int indexOfExtension(String filename){
        if(filename == null){
            return -1;
        }
        int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
        int lastSeparator = indexOfLastSeparator(filename);
        return lastSeparator > extensionPos ? -1 : extensionPos;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the prefix from a full filename, such as <code>C:/</code> or <code>~/</code>.
     * <p>
     * This method will handle a file in either Unix or Windows format. The prefix includes the
     * first slash in the full filename where applicable.
     * 
     * <pre>
     * Windows:
     * a\b\c.txt           --> ""          --> relative
     * \a\b\c.txt          --> "\"         --> current drive absolute
     * C:a\b\c.txt         --> "C:"        --> drive relative
     * C:\a\b\c.txt        --> "C:\"       --> absolute
     * \\server\a\b\c.txt  --> "\\server\" --> UNC
     * 
     * Unix:
     * a/b/c.txt           --> ""          --> relative
     * /a/b/c.txt          --> "/"         --> absolute
     * ~/a/b/c.txt         --> "~/"        --> current user
     * ~                   --> "~/"        --> current user (slash added)
     * ~user/a/b/c.txt     --> "~user/"    --> named user
     * ~user               --> "~user/"    --> named user (slash added)
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on. ie. both
     * Unix and Windows prefixes are matched regardless.
     * 
     * @param filename the filename to query, null returns null
     * @return the prefix of the file, null if invalid
     */
    private static String getPrefix(String filename){
        if(filename == null){
            return null;
        }
        int len = getPrefixLength(filename);
        if(len < 0){
            return null;
        }
        if(len > filename.length()){
            return filename + SYSTEM_SEPARATOR; // we know this only happens for unix
        }
        return filename.substring(0, len);
    }

    /**
     * Gets the path from a full filename, which excludes the prefix.
     * <p>
     * This method will handle a file in either Unix or Windows format. The method is entirely text
     * based, and returns the text before and including the last forward or backslash.
     * 
     * <pre>
     * C:\a\b\c.txt --> a\b\
     * ~/a/b/c.txt  --> a/b/
     * a.txt        --> ""
     * a/b/c        --> a/b/
     * a/b/c/       --> a/b/c/
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * <p>
     * This method drops the prefix from the result. See {@link #getFullPath(String)} for the method
     * that retains the prefix.
     * 
     * @param filename the filename to query, null returns null
     * @return the path of the file, an empty string if none exists, null if invalid
     */
    public static String getPath(String filename){
        if(filename == null){
            return null;
        }
        int prefix = getPrefixLength(filename);
        if(prefix < 0){
            return null;
        }
        int index = indexOfLastSeparator(filename);
        int endIndex = index + 1;
        if(prefix >= filename.length() || index < 0 || prefix >= endIndex){
            return "";
        }
        return filename.substring(prefix, endIndex);
    }

    /**
     * Gets the full path from a full filename, which is the prefix + path.
     * <p>
     * This method will handle a file in either Unix or Windows format. The method is entirely text
     * based, and returns the text before and including the last forward or backslash.
     * 
     * <pre>
     * C:\a\b\c.txt --> C:\a\b\
     * ~/a/b/c.txt  --> ~/a/b/
     * a.txt        --> ""
     * a/b/c        --> a/b/
     * a/b/c/       --> a/b/c/
     * C:           --> C:
     * C:\          --> C:\
     * ~            --> ~/
     * ~/           --> ~/
     * ~user        --> ~user/
     * ~user/       --> ~user/
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * 
     * @param filename the filename to query, null returns null
     * @return the path of the file, an empty string if none exists, null if invalid
     */
    public static String getFullPath(String filename){
        if(filename == null){
            return null;
        }
        int prefix = getPrefixLength(filename);
        if(prefix < 0){
            return null;
        }
        if(prefix >= filename.length()){
            return getPrefix(filename); // add end slash if necessary
        }
        int index = indexOfLastSeparator(filename);
        if(index < 0){
            return filename.substring(0, prefix);
        }
        int end = index + 1;
        if(end == 0){
            end++;
        }
        return filename.substring(0, end);
    }

    /**
     * Gets the name minus the path from a full filename.
     * <p>
     * This method will handle a file in either Unix or Windows format. The text after the last
     * forward or backslash is returned.
     * 
     * <pre>
     * a/b/c.txt --> c.txt
     * a.txt     --> a.txt
     * a/b/c     --> c
     * a/b/c/    --> ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * 
     * @param filename the filename to query, null returns null
     * @return the name of the file without the path, or an empty string if none exists
     */
    public static String getName(String filename){
        if(filename == null){
            return null;
        }
        int index = indexOfLastSeparator(filename);
        return filename.substring(index + 1);
    }

    /**
     * Gets the base name, minus the full path and extension, from a full filename.
     * <p>
     * This method will handle a file in either Unix or Windows format. The text after the last
     * forward or backslash and before the last dot is returned.
     * 
     * <pre>
     * a/b/c.txt --> c
     * a.txt     --> a
     * a/b/c     --> c
     * a/b/c/    --> ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * 
     * @param filename the filename to query, null returns null
     * @return the name of the file without the path, or an empty string if none exists
     */
    public static String getBaseName(String filename){
        return removeExtension(getName(filename));
    }

    /**
     * Gets the extension of a filename.
     * <p>
     * This method returns the textual part of the filename after the last dot. There must be no
     * directory separator after the dot.
     * 
     * <pre>
     * foo.txt      --> "txt"
     * a/b/c.jpg    --> "jpg"
     * a/b.txt/c    --> ""
     * a/b/c        --> ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * 
     * @param filename the filename to retrieve the extension of.
     * @return the extension of the file or an empty string if none exists or {@code null} if the
     *         filename is {@code null}.
     */
    public static String getExtension(String filename){
        if(filename == null){
            return null;
        }
        int index = indexOfExtension(filename);
        if(index == -1){
            return "";
        }else{
            return filename.substring(index + 1);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Removes the extension from a filename.
     * <p>
     * This method returns the textual part of the filename before the last dot. There must be no
     * directory separator after the dot.
     * 
     * <pre>
     * foo.txt    --> foo
     * a\b\c.jpg  --> a\b\c
     * a\b\c      --> a\b\c
     * a.b\c      --> a.b\c
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * 
     * @param filename the filename to query, null returns null
     * @return the filename minus the extension
     */
    public static String removeExtension(String filename){
        if(filename == null){
            return null;
        }
        int index = indexOfExtension(filename);
        if(index == -1){
            return filename;
        }else{
            return filename.substring(0, index);
        }
    }

}
