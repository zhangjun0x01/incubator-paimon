/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.table.store.utils;

import org.apache.flink.table.store.data.InternalRow;
import org.apache.flink.table.store.types.RowType;

import java.util.stream.IntStream;

/** Convert {@link InternalRow} to object array. */
public class RowDataToObjectArrayConverter {

    private final RowType rowType;
    private final InternalRow.FieldGetter[] fieldGetters;

    public RowDataToObjectArrayConverter(RowType rowType) {
        this.rowType = rowType;
        this.fieldGetters =
                IntStream.range(0, rowType.getFieldCount())
                        .mapToObj(
                                i ->
                                        RowDataUtils.createNullCheckingFieldGetter(
                                                rowType.getTypeAt(i), i))
                        .toArray(InternalRow.FieldGetter[]::new);
    }

    public RowType rowType() {
        return rowType;
    }

    public int getArity() {
        return fieldGetters.length;
    }

    public Object[] convert(InternalRow rowData) {
        Object[] result = new Object[fieldGetters.length];
        for (int i = 0; i < fieldGetters.length; i++) {
            result[i] = fieldGetters[i].getFieldOrNull(rowData);
        }
        return result;
    }
}
