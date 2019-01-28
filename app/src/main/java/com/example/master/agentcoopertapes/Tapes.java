package com.example.master.agentcoopertapes;

public class Tapes {

            private String transcription;
            private String ChapterName;
            private int timerStar;
            private int timerend;
            private int tapenumber;

    public Tapes(String Chapter, String transcription, int timerStar, int timerend, int tapenumber) {

        this.ChapterName = Chapter;
        this.transcription = transcription;
        this.timerStar = timerStar;
        this.timerend = timerend;
        this.tapenumber = tapenumber;
    }



    public String getChapterName() {
        return ChapterName;
    }

    public String getTranscription() { return transcription; }

    public int getTimerStar() {
        return timerStar;
    }

    public int getTimerend() {
        return timerend;
    }

    public int getTapenumber() {
        return tapenumber;
    }
}
