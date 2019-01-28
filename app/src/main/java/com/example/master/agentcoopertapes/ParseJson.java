package com.example.master.agentcoopertapes;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ParseJson {


    public List<Tapes> readJsonStream(InputStream in) throws IOException
    {
        JsonReader reader= new JsonReader(new InputStreamReader(in,"UTF-8"));

        try
        {
         return  readTapesArray(reader);
        }
        finally {
            reader.close();
        }


    }

    private List<Tapes> readTapesArray(JsonReader reader) throws IOException
    {
    List<Tapes> coopTapes= new ArrayList<Tapes>();

    reader.beginArray();
    while(reader.hasNext())
    {
        coopTapes.add(readTapes(reader));
    }
    reader.endArray();
    return coopTapes;
    }

    private Tapes readTapes(JsonReader reader) throws IOException {

          String ChapterName=null;
         String transcription=null;
        int timerStar=0;
        int timerend=0;
         int tapenumber=0;

         reader.beginObject();

         while(reader.hasNext())
         {
             String name=reader.nextName();

             if(name.equals(("ChapterName")))
             {
                 ChapterName=reader.nextString();
             }
            else if(name.equals(("transcription")))
             {
                 transcription=reader.nextString();
             }
             else if(name.equals("timerStar"))
             {
                 timerStar=reader.nextInt();
             }
             else if(name.equals("timerend"))
             {
                 timerend=reader.nextInt();
             }
             else if(name.equals("tapeNumber"))
             {
                 tapenumber=reader.nextInt();
             }
             else {reader.skipValue();}

         }
        reader.endObject();
         return  new Tapes(ChapterName,transcription,timerStar,timerend,tapenumber);
    }

}
