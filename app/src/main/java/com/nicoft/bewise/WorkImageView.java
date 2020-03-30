package com.nicoft.bewise;

import android.content.Context;
import android.util.AttributeSet;

import com.github.siyamed.shapeimageview.ShaderImageView;
import com.github.siyamed.shapeimageview.shader.ShaderHelper;
import com.github.siyamed.shapeimageview.shader.SvgShader;


/**
 * Created by Nicolas on 08/10/2015.
 */
public class WorkImageView extends ShaderImageView {
    public WorkImageView(Context context) {
        super(context);
    }

    public WorkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WorkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        return new SvgShader(R.raw.suitcase);
    }
}
