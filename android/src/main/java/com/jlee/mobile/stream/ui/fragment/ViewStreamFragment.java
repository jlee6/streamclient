package com.jlee.mobile.stream.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;

import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.demo.SmoothStreamingTestMediaDrmCallback;
import com.google.android.exoplayer.demo.WidevineTestMediaDrmCallback;
import com.google.android.exoplayer.demo.player.DashRendererBuilder;
import com.google.android.exoplayer.demo.player.DemoPlayer;
import com.google.android.exoplayer.demo.player.ExtractorRendererBuilder;
import com.google.android.exoplayer.demo.player.HlsRendererBuilder;
import com.google.android.exoplayer.demo.player.SmoothStreamingRendererBuilder;
import com.google.android.exoplayer.drm.UnsupportedDrmException;
import com.google.android.exoplayer.metadata.GeobMetadata;
import com.google.android.exoplayer.metadata.PrivMetadata;
import com.google.android.exoplayer.metadata.TxxxMetadata;
import com.google.android.exoplayer.util.Util;
import com.jlee.mobile.stream.Constants;
import com.jlee.mobile.stream.R;
import com.jlee.mobile.stream.module.StreamEndPoint;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A google exo player based adaptive stream viewer
 */
public class ViewStreamFragment extends BaseFragment {
    private static final String TAG = ViewStreamFragment.class.getName();

    @Bind(R.id.surface_view)
    SurfaceView surfaceView;

    @Bind(R.id.fab)
    FloatingActionButton btnFloat;

    private Context context;

    private MediaController mediaController;
    private DemoPlayer player;

    private long playerPosition;
    private boolean playerNeedsPrepare;
    private StreamEndPoint content;

    public ViewStreamFragment() {
        fragmentId = "view stream fragment";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            content = getArguments().getParcelable(Constants.ARGUMENT_CONTENT);
        }

        content = new StreamEndPoint(Constants.TYPE_HLS, "Apple master playlist",
//                Uri.parse("https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8"));
        Uri.parse("https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8"));
//        Uri.parse("https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear0/prog_index.m3u8"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_stream, container, false);
        ButterKnife.bind(this, view);

        surfaceView.getHolder().addCallback(surfaceHolderCallback);

        mediaController = new MediaController(context);
        mediaController.setAnchorView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (player == null) {
            preparePlayer(true);
        } else {
            player.setBackgrounded(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        releasePlayer();
    }

    private DemoPlayer.RendererBuilder getRendererBuilder(StreamEndPoint endPoint) {
        String userAgent = Util.getUserAgent(context, "ExoPlayerDemo");
        switch (endPoint.getTypeId()) {
            case Constants.TYPE_SS:
                return new SmoothStreamingRendererBuilder(context, userAgent, endPoint.getUri().toString(),
                        new SmoothStreamingTestMediaDrmCallback());
            case Constants.TYPE_DASH:
                return new DashRendererBuilder(context, userAgent, endPoint.getUri().toString(),
                        new WidevineTestMediaDrmCallback(String.valueOf(endPoint.getTypeId())));
            case Constants.TYPE_HLS:
                return new HlsRendererBuilder(context, userAgent, endPoint.getUri().toString());
            case Constants.TYPE_OTHER:
                return new ExtractorRendererBuilder(context, userAgent, endPoint.getUri());
            default:
                throw new IllegalStateException("Unsupported type: " + endPoint.getTypeId());
        }
    }

//        // User controls
//        private void updateButtonVisibilities() {
//            retryButton.setVisibility(playerNeedsPrepare ? View.VISIBLE : View.GONE);
//            videoButton.setVisibility(haveTracks(DemoPlayer.TYPE_VIDEO) ? View.VISIBLE : View.GONE);
//            audioButton.setVisibility(haveTracks(DemoPlayer.TYPE_AUDIO) ? View.VISIBLE : View.GONE);
//            textButton.setVisibility(haveTracks(DemoPlayer.TYPE_TEXT) ? View.VISIBLE : View.GONE);
//        }
//
//        private boolean haveTracks(int type) {
//            return player != null && player.getTrackCount(type) > 0;
//        }
//
//        public void showVideoPopup(View v) {
//            PopupMenu popup = new PopupMenu(this, v);
//            configurePopupWithTracks(popup, null, DemoPlayer.TYPE_VIDEO);
//            popup.show();
//        }
//
//        public void showAudioPopup(View v) {
//            PopupMenu popup = new PopupMenu(this, v);
//            Menu menu = popup.getMenu();
//            menu.add(Menu.NONE, Menu.NONE, Menu.NONE, R.string.enable_background_audio);
//            final MenuItem backgroundAudioItem = menu.findItem(0);
//            backgroundAudioItem.setCheckable(true);
//            backgroundAudioItem.setChecked(enableBackgroundAudio);
//            OnMenuItemClickListener clickListener = new OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    if (item == backgroundAudioItem) {
//                        enableBackgroundAudio = !item.isChecked();
//                        return true;
//                    }
//                    return false;
//                }
//            };
//            configurePopupWithTracks(popup, clickListener, DemoPlayer.TYPE_AUDIO);
//            popup.show();
//        }
//
//        public void showTextPopup(View v) {
//            PopupMenu popup = new PopupMenu(this, v);
//            configurePopupWithTracks(popup, null, DemoPlayer.TYPE_TEXT);
//            popup.show();
//        }
//
//        public void showVerboseLogPopup(View v) {
//            PopupMenu popup = new PopupMenu(this, v);
//            Menu menu = popup.getMenu();
//            menu.add(Menu.NONE, 0, Menu.NONE, R.string.logging_normal);
//            menu.add(Menu.NONE, 1, Menu.NONE, R.string.logging_verbose);
//            menu.setGroupCheckable(Menu.NONE, true, true);
//            menu.findItem((VerboseLogUtil.areAllTagsEnabled()) ? 1 : 0).setChecked(true);
//            popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    if (item.getItemId() == 0) {
//                        VerboseLogUtil.setEnableAllTags(false);
//                    } else {
//                        VerboseLogUtil.setEnableAllTags(true);
//                    }
//                    return true;
//                }
//            });
//            popup.show();
//        }
//
//        private void configurePopupWithTracks(PopupMenu popup,
//                                              final OnMenuItemClickListener customActionClickListener,
//                                              final int trackType) {
//            if (player == null) {
//                return;
//            }
//            int trackCount = player.getTrackCount(trackType);
//            if (trackCount == 0) {
//                return;
//            }
//            popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    return (customActionClickListener != null
//                            && customActionClickListener.onMenuItemClick(item))
//                            || onTrackItemClick(item, trackType);
//                }
//            });
//            Menu menu = popup.getMenu();
//            // ID_OFFSET ensures we avoid clashing with Menu.NONE (which equals 0)
//            menu.add(MENU_GROUP_TRACKS, DemoPlayer.TRACK_DISABLED + ID_OFFSET, Menu.NONE, R.string.off);
//            for (int i = 0; i < trackCount; i++) {
//                menu.add(MENU_GROUP_TRACKS, i + ID_OFFSET, Menu.NONE,
//                        buildTrackName(player.getTrackFormat(trackType, i)));
//            }
//            menu.setGroupCheckable(MENU_GROUP_TRACKS, true, true);
//            menu.findItem(player.getSelectedTrack(trackType) + ID_OFFSET).setChecked(true);
//        }
//
//        private static String buildTrackName(MediaFormat format) {
//            if (format.adaptive) {
//                return "auto";
//            }
//            String trackName;
//            if (MimeTypes.isVideo(format.mimeType)) {
//                trackName = joinWithSeparator(joinWithSeparator(buildResolutionString(format),
//                        buildBitrateString(format)), buildTrackIdString(format));
//            } else if (MimeTypes.isAudio(format.mimeType)) {
//                trackName = joinWithSeparator(joinWithSeparator(joinWithSeparator(buildLanguageString(format),
//                                buildAudioPropertyString(format)), buildBitrateString(format)),
//                        buildTrackIdString(format));
//            } else {
//                trackName = joinWithSeparator(joinWithSeparator(buildLanguageString(format),
//                        buildBitrateString(format)), buildTrackIdString(format));
//            }
//            return trackName.length() == 0 ? "unknown" : trackName;
//        }
//
//        private static String buildResolutionString(MediaFormat format) {
//            return format.width == MediaFormat.NO_VALUE || format.height == MediaFormat.NO_VALUE
//                    ? "" : format.width + "x" + format.height;
//        }
//
//        private static String buildAudioPropertyString(MediaFormat format) {
//            return format.channelCount == MediaFormat.NO_VALUE || format.sampleRate == MediaFormat.NO_VALUE
//                    ? "" : format.channelCount + "ch, " + format.sampleRate + "Hz";
//        }
//
//        private static String buildLanguageString(MediaFormat format) {
//            return TextUtils.isEmpty(format.language) || "und".equals(format.language) ? ""
//                    : format.language;
//        }
//
//        private static String buildBitrateString(MediaFormat format) {
//            return format.bitrate == MediaFormat.NO_VALUE ? ""
//                    : String.format(Locale.US, "%.2fMbit", format.bitrate / 1000000f);
//        }
//
//        private static String joinWithSeparator(String first, String second) {
//            return first.length() == 0 ? second : (second.length() == 0 ? first : first + ", " + second);
//        }
//
//        private static String buildTrackIdString(MediaFormat format) {
//            return format.trackId == MediaFormat.NO_VALUE ? ""
//                    : String.format(Locale.US, " (%d)", format.trackId);
//        }
//
//        private boolean onTrackItemClick(MenuItem item, int type) {
//            if (player == null || item.getGroupId() != MENU_GROUP_TRACKS) {
//                return false;
//            }
//            player.setSelectedTrack(type, item.getItemId() - ID_OFFSET);
//            return true;
//        }
//
//        private void toggleControlsVisibility()  {
//            if (mediaController.isShowing()) {
//                mediaController.hide();
//                debugRootView.setVisibility(View.GONE);
//            } else {
//                showControls();
//            }
//        }
//
//        private void showControls() {
//            mediaController.show(0);
//            debugRootView.setVisibility(View.VISIBLE);
//        }
//
//        // DemoPlayer.CaptionListener implementation
//
//        @Override
//        public void onCues(List<Cue> cues) {
//            subtitleLayout.setCues(cues);
//        }
//

    private void preparePlayer(boolean playWhenReady) {
        if (player == null) {
            player = new DemoPlayer(getRendererBuilder(content));

            player.addListener(playerListener);
            player.setMetadataListener(metadataListener);
            player.seekTo(playerPosition);
            playerNeedsPrepare = true;

            mediaController.setMediaPlayer(player.getPlayerControl());
            mediaController.setEnabled(true);
        }
        if (playerNeedsPrepare) {
            player.prepare();
            playerNeedsPrepare = false;
        }

        player.setSurface(surfaceView.getHolder().getSurface());
        player.setPlayWhenReady(playWhenReady);
    }

    private void releasePlayer() {
        if (player != null) {
            playerPosition = player.getCurrentPosition();
            player.release();
            player = null;
        }
    }


    DemoPlayer.Id3MetadataListener metadataListener = (Map<String, Object> metadata) -> {
        for (Map.Entry<String, Object> entry : metadata.entrySet()) {
            if (TxxxMetadata.TYPE.equals(entry.getKey())) {
                TxxxMetadata txxxMetadata = (TxxxMetadata) entry.getValue();
                Log.i(TAG, String.format("ID3 TimedMetadata %s: description=%s, value=%s",
                        TxxxMetadata.TYPE, txxxMetadata.description, txxxMetadata.value));
            } else if (PrivMetadata.TYPE.equals(entry.getKey())) {
                PrivMetadata privMetadata = (PrivMetadata) entry.getValue();
                Log.i(TAG, String.format("ID3 TimedMetadata %s: owner=%s",
                        PrivMetadata.TYPE, privMetadata.owner));
            } else if (GeobMetadata.TYPE.equals(entry.getKey())) {
                GeobMetadata geobMetadata = (GeobMetadata) entry.getValue();
                Log.i(TAG, String.format("ID3 TimedMetadata %s: mimeType=%s, filename=%s, description=%s",
                        GeobMetadata.TYPE, geobMetadata.mimeType, geobMetadata.filename,
                        geobMetadata.description));
            } else {
                Log.i(TAG, String.format("ID3 TimedMetadata %s", entry.getKey()));
            }
        }
    };

    SurfaceHolder.Callback surfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            if (player != null) {
                player.setSurface(surfaceHolder.getSurface());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            // Nothing
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if (player != null) {
                player.blockingClearSurface();
            }
        }
    };

    DemoPlayer.Listener playerListener = new DemoPlayer.Listener() {
        @Override
        public void onStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == ExoPlayer.STATE_ENDED) {
//                showControls();
            }

            String text = "playWhenReady=" + playWhenReady + ", playbackState=";
            switch (playbackState) {
                case ExoPlayer.STATE_BUFFERING:
                    text += "buffering";
                    break;
                case ExoPlayer.STATE_ENDED:
                    text += "ended";
                    break;
                case ExoPlayer.STATE_IDLE:
                    text += "idle";
                    btnFloat.setImageDrawable(context.getDrawable(R.drawable.ic_play_circle_outline_black));
                    break;
                case ExoPlayer.STATE_PREPARING:
                    text += "preparing";
                    btnFloat.setImageDrawable(context.getDrawable(R.drawable.ic_pause_circle_outline_black));
                    break;
                case ExoPlayer.STATE_READY:
                    text += "ready";
                    break;
                default:
                    text += "unknown";
                    break;
            }

            Log.wtf(TAG, String.format("Player state: %s", text));
//            playerStateTextView.setText(text);
        }

        @Override
        public void onError(Exception e) {
            if (e instanceof UnsupportedDrmException) {
                // Special case DRM failures.
                UnsupportedDrmException unsupportedDrmException = (UnsupportedDrmException) e;
                String error = Util.SDK_INT < 18 ? "DRM is not supported in this version"
                        : unsupportedDrmException.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
                        ? "Unsupported DRM scheme" : "Unknown DRM scheme";
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }

//          playerNeedsPrepare = true;
//            updateButtonVisibilities();
//            showControls();
        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
//            shutterView.setVisibility(View.GONE);
//            videoFrame.setAspectRatio(
//                    height == 0 ? 1 : (width * pixelWidthAspectRatio) / height);
        }
    };

//        private void configureSubtitleView() {
//            CaptionStyleCompat style;
//            float fontScale;
//            if (Util.SDK_INT >= 19) {
//                style = getUserCaptionStyleV19();
//                fontScale = getUserCaptionFontScaleV19();
//            } else {
//                style = CaptionStyleCompat.DEFAULT;
//                fontScale = 1.0f;
//            }
//            subtitleLayout.setStyle(style);
//            subtitleLayout.setFractionalTextSize(SubtitleLayout.DEFAULT_TEXT_SIZE_FRACTION * fontScale);
//        }
//
//        @TargetApi(19)
//        private float getUserCaptionFontScaleV19() {
//            CaptioningManager captioningManager =
//                    (CaptioningManager) getSystemService(Context.CAPTIONING_SERVICE);
//            return captioningManager.getFontScale();
//        }
//
//        @TargetApi(19)
//        private CaptionStyleCompat getUserCaptionStyleV19() {
//            CaptioningManager captioningManager =
//                    (CaptioningManager) getSystemService(Context.CAPTIONING_SERVICE);
//            return CaptionStyleCompat.createFromCaptionStyle(captioningManager.getUserStyle());
//        }
//
//    }

    @OnClick(R.id.fab)
    void onShowFabSnackbar(View view) {
        Snackbar.make(view, "Snack bar tied to floating action button", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
    }
}
