package com.example.cabbooking.driver.other;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class ImageCaching {

    public static void loadImage(final ImageView imageView, final Activity activity, final String url) {
        try {
            if (imageView != null && activity != null && url != null && url.length() > 0) {

                Picasso.with(activity)
                        .load(url)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(imageView, new Callback.EmptyCallback() {
                            @Override
                            public void onSuccess() {
                                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                                try {
                                    imageView.setImageBitmap(RoundImage.getCircularBitmap(bitmap));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                catch (Error e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError() {
                                //Try again online if cache failed
                                Picasso.with(activity)
                                        .load(url)
                                        .into(imageView, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                                imageView.setImageBitmap(RoundImage.getCircularBitmap(bitmap));

                                            }

                                            @Override
                                            public void onError() {

                                            }
                                        });
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }
    public static void loadImage1(final ImageView imageView, final Activity activity, final String url) {
        try {
            if (imageView != null && activity != null && url != null && url.length() > 0) {
                    Picasso.with(activity)
                            .load(url)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(imageView, new Callback.EmptyCallback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                                    try {
                                        imageView.setImageBitmap(RoundImage.getCircularBitmap(bitmap));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } catch (Error e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError() {
                                    //Try again online if cache failed
                                    Picasso.with(activity)
                                            .load(url)
                                            .into(imageView, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                                    imageView.setImageBitmap(RoundImage.getCircularBitmap(bitmap));

                                                }

                                                @Override
                                                public void onError() {

                                                }
                                            });
                                }
                            });
                }

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }


    public static void loadImageNorm(final ImageView imv, final Activity activity, final String url) {
        try {
            if (imv != null && activity != null && url != null && url.length() > 0) {

                    Picasso.with(activity)
                            .load(url)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(imv, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    //Try again online if cache failed
                                    Picasso.with(activity)
                                            .load(url)
                                            .into(imv, new Callback() {
                                                @Override
                                                public void onSuccess() {

                                                }

                                                @Override
                                                public void onError() {

                                                }
                                            });
                                }
                            });
                }

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

}
