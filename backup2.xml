<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".StreamingExampleActivity">

    <com.devbrackets.android.exomedia.ui.widget.VideoView
        android:id="@+id/video_view"
        android:layout_width="match_parent"
        android:layout_height="220dp"
        android:paddingTop="10dp"
        app:layout_constraintTop_toTopOf="parent"
        app:useDefaultControls="true"
        tools:layout_editor_absoluteX="0dp" />

    <ProgressBar
        android:id="@+id/pb_status"
        android:layout_width="10dp"
        android:layout_height="10dp"
        android:visibility="gone"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.96"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.977"
        tools:visibility="visible" />

    <Button
        android:id="@+id/btn_start_streaming"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="16dp"
        android:text="@string/btn_start_stream"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.498"
        app:layout_constraintStart_toStartOf="parent" />

    <WebView
        android:id="@+id/webview"
        android:layout_width="match_parent"
        android:layout_height="405dp"
        app:layout_constraintBottom_toTopOf="@+id/btn_start_streaming"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/video_view" />

</androidx.constraintlayout.widget.ConstraintLayout>
