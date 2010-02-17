/*
 * Copyright (C) 2009-2010 Mathias Doenitz
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

package org.parboiled.matchers;

public class DefaultMatcherVisitor<V, R> implements MatcherVisitor<V, R> {

    public R visit(ActionMatcher<V> matcher) {
        return defaultValue(matcher);
    }

    public R visit(CharactersMatcher<V> matcher) {
        return defaultValue(matcher);
    }

    public R visit(CharIgnoreCaseMatcher<V> matcher) {
        return defaultValue(matcher);
    }

    public R visit(CharMatcher<V> matcher) {
        return defaultValue(matcher);
    }

    public R visit(CharRangeMatcher<V> matcher) {
        return defaultValue(matcher);
    }

    public R visit(EmptyMatcher<V> matcher) {
        return defaultValue(matcher);
    }

    public R visit(FirstOfMatcher<V> matcher) {
        return defaultValue(matcher);
    }

    public R visit(OneOrMoreMatcher<V> matcher) {
        return defaultValue(matcher);
    }

    public R visit(OptionalMatcher<V> matcher) {
        return defaultValue(matcher);
    }

    public R visit(SequenceMatcher<V> matcher) {
        return defaultValue(matcher);
    }

    public R visit(TestMatcher<V> matcher) {
        return defaultValue(matcher);
    }

    public R visit(TestNotMatcher<V> matcher) {
        return defaultValue(matcher);
    }

    public R visit(ZeroOrMoreMatcher<V> matcher) {
        return defaultValue(matcher);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public R defaultValue(AbstractMatcher<V> matcher) {
        return null;
    }

}