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

package reader.filter;

import reader.predicate.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter according to given predicates
 * @param <T>   stream for filtering
 * @param <E>   result of filtering
 */
public abstract class AbstractFilter<T, E> {
    protected List<Predicate> predicates = new ArrayList<Predicate>();

    public abstract E filter(T obj);

    /**
     * Add predicate to list of predicates
     * @param predicate
     */
    public void addPredicate(Predicate<T> predicate) {
        predicates.add(predicate);
    }
}
