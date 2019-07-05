`GlideUtil` 的基本使用
````java
   /* Glide 加载 */
        GlideUtils.loadImage(this, "https://avatars1.githubusercontent.com/u/10889046?v=4&s=460",
             mTestImageViewGlide, new GlideUtils.ImageLoadListener<String, GlideDrawable>() {
                    @Override
                    public void onLoadingComplete(String uri, ImageView view, GlideDrawable resource) {
                        view.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadingError(String source, Exception e) {
                        LogUtils.d(e.getMessage());
                    }
                }, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
````