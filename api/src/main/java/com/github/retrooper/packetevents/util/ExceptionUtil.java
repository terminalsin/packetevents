/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.util;

import java.util.Set;

public class ExceptionUtil {
    public static boolean isException(Throwable t, Class<?> clazz) {
        Class<?> throwableClass = t.getClass();
        while (t != null) {
            if (clazz.isAssignableFrom(throwableClass)) {
                return true;
            }

            t = t.getCause();
        }
        return false;
    }

    public static boolean isExceptionContainedIn(Throwable t, Set<Class<? extends Throwable>> exceptions) {
        return exceptions.contains(t.getClass());
    }
}
