
package com.ayia.fxconverter.binding


import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ayia.fxconverter.R
import com.ayia.fxconverter.presentation.converter.ValidatorState
import com.ayia.fxconverter.presentation.converter.isValidAmount
import com.ayia.fxconverter.presentation.converter.isValidCurrency
import com.ayia.fxconverter.util.CircleTransform
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("setFlagImage96")
fun setFlagImage96(iv: ImageView, url:String?){

    Glide.with(iv.context)
        .load(url)
        .apply(RequestOptions.placeholderOf(R.drawable.background_default_image_circle).override(96, 96))
        .transform(CircleTransform())
        .error(R.drawable.background_error_image_circle)
        .placeholder(R.drawable.background_default_image_circle)
        .into(iv)
}



@BindingAdapter("setFlagImage150")
fun setFlagImage150(iv: ImageView, url:String?){
    Glide.with(iv.context)
        .load(url)
        .apply(RequestOptions.placeholderOf(R.drawable.background_default_image_circle))
        .transform(CircleTransform())
        .error(R.drawable.background_error_image_circle)
        .placeholder(R.drawable.background_default_image_circle)
        .into(iv)
}

@BindingAdapter("setTheme")
fun setTheme(tv: TextView, themePos: Int){
    val arrayTheme = tv.context.resources.getStringArray(R.array.themes)
    tv.text = arrayTheme[themePos]
}

@BindingAdapter( value = ["showFromValidation", "to"])
fun showFromValidation(view: TextInputLayout, from: String, to: String?) {

    val validatorState: ValidatorState = isValidCurrency(from, to)

    if (validatorState.isDefault()){
        view.error = null
        view.helperText = view.context.getString(R.string.label_ok)
    }
    else {
        if (validatorState.isValid == true) {
            view.error = null
            view.helperText = view.context.getString(R.string.label_ok)
        } else {
            view.error = view.context.getString(validatorState.errorTxtId!!)
        }
    }

}


@BindingAdapter( value = ["showToValidation", "from"])
fun showToValidation(view: TextInputLayout, to: String?, from: String) {

    val validatorState: ValidatorState = isValidCurrency(from, to)

    if (validatorState.isDefault()){
        view.error = null
        view.helperText = null
    } else {
        if (validatorState.isValid == true) {
            view.error = null
            view.helperText = view.context.getString(R.string.label_ok)
        } else {
            view.error = view.context.getString(validatorState.errorTxtId!!)
        }
    }

}

@BindingAdapter("showAmountValidation")
fun showAmountValidation(view: TextInputLayout, amount: String?) {

    val validatorState: ValidatorState = isValidAmount(amount)

    if (validatorState.isDefault()){
        view.error = null
        view.helperText = null
    }
    else{
        if (validatorState.isValid == true) {
            view.error = null
            view.helperText = view.context.getString(R.string.label_ok)
        } else {
            view.error = view.context.getString(validatorState.errorTxtId!!)
        }
    }

}

