package com.test.simpletest.customview;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

public class SavedState extends View.BaseSavedState {
    int value;

    public SavedState(Parcelable superState) {
        super(superState);
    }
    private SavedState(Parcel in ){
        super(in);
        value = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(value);
    }

    public static final Parcelable.Creator<SavedState> CREATOR=
        new Parcelable.Creator<SavedState>(){
            public SavedState createFromParcel (Parcel in ){
                return new SavedState(in);

            }

            @Override
            public SavedState[] newArray(int i) {
                return new SavedState[0];
            }
        };
}
