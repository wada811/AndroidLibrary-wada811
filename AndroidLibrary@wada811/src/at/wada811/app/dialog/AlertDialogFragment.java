/*
 * Copyright 2013 wada811<at.wada811@gmail.com>
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
package at.wada811.app.dialog;

import jp.co.noxi.app.NXAlertDialog;
import android.view.View;
import android.widget.ListAdapter;

public class AlertDialogFragment extends NXAlertDialog {

    /**
     * Delegate for list adapter bindings.
     */
    interface ListAdapterDelegate {
        ListAdapter getListAdapter(DialogFragmentInterface dialog);
    }

    /**
     * Delegate for custom title view.
     */
    interface TitleViewDelegate {
        View getTitleView(DialogFragmentInterface dialog);
    }
}
