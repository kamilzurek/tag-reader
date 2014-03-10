/*
* Copyright 2014 - Kamil Zurek
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package reader.count.impl;

import reader.count.Counter;

/**
 * Count elements only form 0 to given <i>count</i>
 */
public class OnlyCounter implements Counter {
    private final int size;

    public OnlyCounter(final int size) {
        this.size = size;
    }

    public boolean check(int count) {
        return count < size;
    }
}
