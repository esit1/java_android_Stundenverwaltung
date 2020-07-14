package de.christine.stundenverwaltung.createPDF;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import de.christine.stundenverwaltung.convert.ConvertString;
import de.christine.stundenverwaltung.model.DayEntry;
import de.christine.stundenverwaltung.model.PreferencesKeys;

public class CreatePDF {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void createPdf(Activity activity,List<DayEntry> dataSource, String month) {

        SharedPreferences preferences = activity.getSharedPreferences(PreferencesKeys.PrefKeys.PREF_NAME, Context.MODE_PRIVATE);

        String companyName = (preferences.getString(PreferencesKeys.PrefKeys.PREF_COMPANYNAME, ""));
        String userName = (preferences.getString(PreferencesKeys.PrefKeys.PREF_USERNAME, ""));


        //int dataSourceSize = dataSource.size();
        final int PAGEWIDTH = 595;
        final int PAGEHEIGHT = 842;

        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(PAGEWIDTH, PAGEHEIGHT, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        paint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, PAGEWIDTH, 80, paint);

        //Überschrift
        paint.setColor(Color.BLACK);
        paint.setTextSize(24);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);


        canvas.drawText("Stundenzettel", PAGEWIDTH/2 - 10, 40, paint);

        //Information, Firma, Monat, Mitarbeiter
        paint.setColor(Color.BLACK);
        paint.setTextSize(15);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setAntiAlias(true);

        canvas.drawText("Firma: ", 120, 100, paint);
        canvas.drawText("Mitarbeiter: ", 120, 140, paint);
        canvas.drawText("Monat: ", 120, 180, paint);

        canvas.drawText(companyName, 400, 100, paint);
        canvas.drawText(userName, 400, 140, paint);
        canvas.drawText(month, 400, 180, paint);

        //Übersicht Überschrift
        paint.setColor(Color.GRAY);
        canvas.drawRect(0, 240, PAGEWIDTH, 200, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);

        canvas.drawText("Übersicht", PAGEWIDTH/2-10, 230, paint);

        //Überschrift, Datum, Start usw.
        paint.setColor(Color.BLACK);
        paint.setTextSize(14);
        paint.setUnderlineText(true);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setAntiAlias(true);

        canvas.drawText("Datum", 30, 260, paint);
        canvas.drawText("Startzeit", 110, 260, paint);
        canvas.drawText("Endzeit", 190, 260, paint);
        canvas.drawText("Pause", 270, 260, paint);
        canvas.drawText("Gesamtzeit", 350, 260, paint);
        canvas.drawText("Bemerkung", 430, 260, paint);

        paint.setTextSize(12);
        paint.setUnderlineText(false);
        int yEntry = 280;
        int totalTimeInMinute = 0;


        for (int i = 0; i < dataSource.size(); i++) {

            paint.setColor(Color.GRAY);
            canvas.drawLine(0, yEntry + 7, PAGEWIDTH, yEntry + 7, paint);
            paint.setColor(Color.BLACK);

            canvas.drawText(dataSource.get(i).getDateToString(), 30, yEntry, paint);
            canvas.drawText(dataSource.get(i).getStartTime(), 110, yEntry, paint);
            canvas.drawText(dataSource.get(i).getEndTime(), 190, yEntry, paint);
            canvas.drawText(dataSource.get(i).getBreakTime(), 270, yEntry, paint);
            canvas.drawText(dataSource.get(i).getTotalTime(), 350, yEntry, paint);
            canvas.drawText(dataSource.get(i).getComment(), 430, yEntry, paint);

            yEntry = yEntry + 20;

            totalTimeInMinute = totalTimeInMinute + ConvertString.timeToIntInMinute(dataSource.get(i).getTotalTime());

        }

        int totalTimeRestMinute = totalTimeInMinute % 60;
        int totalTimeHour = totalTimeInMinute / 60;

        canvas.drawText("Stunden im Monat: ", 120, yEntry + 40, paint);
        canvas.drawText(ConvertString.timeToString(totalTimeHour, totalTimeRestMinute) + " Stunden", 350, yEntry + 40, paint);


        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page

        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Stundenzettel/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path + "Stundenzettel" + month + ".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(activity, "PDF erstellt. Speicherort: " + directory_path, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error " + e.toString());
            Toast.makeText(activity, "Fehlermeldung " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }
}
