package br.com.recrutamentoandroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.recrutamentoandroid.api.TraktApi;
import br.com.recrutamentoandroid.model.Episode;
import br.com.recrutamentoandroid.model.Rating;
import br.com.recrutamentoandroid.utils.Constants;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private ListView episodesListView;

    private RelativeLayout bottomContainer;

    private TextView ratingTextView;

    private ImageView seasonImage;

    private ProgressBar spinner;

    private String ratingStr = "";

    static class ViewHolder {
        public TextView title;
        public TextView number;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* OBS:
         * 1 - getting images from Trakt was not working. It was giving an Access denied message. So I got an image from web to represent the header image
         * 2 - Unfortunately I could not position the thumbnail image (vertical image) properly. I decided to not show it. */

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        episodesListView = (ListView) findViewById(R.id.episodes_list);

        ratingTextView = (TextView) findViewById(R.id.rating_text);

        spinner = (ProgressBar) findViewById(R.id.main_progressbar);

        seasonImage = (ImageView) findViewById(R.id.season_image);

        bottomContainer = (RelativeLayout) findViewById(R.id.bottom_ct);

        String showId = "game-of-thrones", season = "1";

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.TRAKT_BASE_URI).build();

        TraktApi trakt = restAdapter.create(TraktApi.class);

        trakt.getSeasonRating(showId, season, Constants.TRAKT_API_VERSION,
                Constants.TRAKT_ID, Constants.TRAKT_CONTENT_TYPE, new Callback<Rating>() {

                    @Override
                    public void success(Rating rating, Response response) {
                        ratingStr = String.format("%.1f", rating.getRating());
                        ratingTextView.setText(ratingStr);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ratingStr = "-";
                        ratingTextView.setText(ratingStr);
                    }
                });

        Picasso.with(MainActivity.this).load("https://hammyreviews.files.wordpress.com/2015/04/game-of-thrones-season-4.jpg").
                placeholder(R.drawable.season_background_placeholder).into(seasonImage);

        trakt.getSeason(showId, season, Constants.TRAKT_API_VERSION,
                Constants.TRAKT_ID, Constants.TRAKT_CONTENT_TYPE, new Callback<ArrayList<Episode>>() {

                    @Override
                    public void success(ArrayList<Episode> episodes, Response response) {

                        bottomContainer.setVisibility(View.VISIBLE);
                        episodesListView.setVisibility(View.VISIBLE);
                        seasonImage.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.GONE);

                        // Create adapter
                        EpisodesArrayAdapter episodesAdapter = new EpisodesArrayAdapter(MainActivity.this, episodes);
                        episodesListView.setAdapter(episodesAdapter);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getApplication(), "Some error occurred. Try again.", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    class EpisodesArrayAdapter extends ArrayAdapter<Episode> {

        private final Activity context;

        private ArrayList<Episode> episodes;

        public EpisodesArrayAdapter(Activity context, ArrayList<Episode> episodes) {
            super(context, R.layout.episode_list_item, episodes);
            this.context = context;
            this.episodes = episodes;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = convertView;
            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                rowView = inflater.inflate(R.layout.episode_list_item, null);
                // configure view holder
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) rowView.findViewById(R.id.episode_title);
                viewHolder.number = (TextView) rowView.findViewById(R.id.episode_number);

                rowView.setTag(viewHolder);
            }

            // fill data
            ViewHolder holder = (ViewHolder) rowView.getTag();

            String title = episodes.get(position).getTitle();
            holder.title.setText(title);

            String number = "E" + episodes.get(position).getNumber();
            holder.number.setText(number);

            return rowView;
        }
    }
}
