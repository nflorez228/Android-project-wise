package com.nicoft.bewise;

import android.content.Context;
import android.util.AttributeSet;

import com.github.siyamed.shapeimageview.ShaderImageView;
import com.github.siyamed.shapeimageview.shader.ShaderHelper;
import com.github.siyamed.shapeimageview.shader.SvgShader;


/**
 * Created by Nicolas on 08/10/2015.
 */
public class HouseImageView extends ShaderImageView {
    public HouseImageView(Context context) {
        super(context);
    }

    public HouseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HouseImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        return new SvgShader(R.raw.house);
    }
}
