package spotifyAPI;
import interfaces.SpotifyPlayerAPI;
import model.Artist;
import model.Track;
import utils.OSXUtils;
import utils.SpotifyUtils;
import java.io.IOException;
import java.util.List;

public class SpotifyAppleScriptWrapper implements SpotifyPlayerAPI {
    public void playTrack(String id){
        try {
            OSXUtils.runAppleCmd("tell application \"Spotify\"\n" +
                    "     play track \""+id+"\"\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playTrack(Track song) {
        playTrack(song.getUri());
    }

    public boolean isSingle()
    {
       /* String str = getTrackInfo();
        assert str != null;
        if(str.contains("\"album_type\":\"single\""))
            return true;*/
        return false;
    }
    private void increaseVolume(int am)
    {
        try {
            OSXUtils.runAppleCmd("tell application \"Spotify\"\n" +
                    "\tset currentvol to get sound volume\n" +
                    "\tif currentvol > "+ (100-am)+" then\n" +
                    "\t\tset sound volume to 100\n" +
                    "\telse\n" +
                    "\t\tset sound volume to currentvol + "+am+"\n" +
                    "\tend if\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void decreaseVolume(int am)
    {
        try {
            OSXUtils.runAppleCmd("tell application \"Spotify\"\n" +
                    "    set currentvol to get sound volume\n" +
                    "    -- volume wraps at 100 to 0\n" +
                    "    if currentvol < " + am +" then\n" +
                    "        set sound volume to 0\n" +
                    "    else\n" +
                    "        set sound volume to currentvol - "+am+"\n" +
                    "    end if\n" +
                    "end tell\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setVolume(int vol) {
        try {
            OSXUtils.runAppleCmd("tell application \"Spotify\"\n" +
                    "\tset sound volume to" +vol+"\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playPause()
    {
        try {
            OSXUtils.runAppleCmd("\n" +
                    "tell application \"Spotify\"\n" +
                    "\tplaypause\n" +
                    "end tell\n" +
                    "\n" +
                    "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void play()
    {
        try {
            OSXUtils.runAppleCmd("\n" +
                    "tell application \"Spotify\"\n" +
                    "\tplay\n" +
                    "end tell\n" +
                    "\n" +
                    "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public  void pause()
    {
        try {
            OSXUtils.runAppleCmd("\n" +
                    "tell application \"Spotify\"\n" +
                    "\tpause\n" +
                    "end tell\n" +
                    "\n" +
                    "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public long getPlayBackPosition()
    {
        try {
            String str = OSXUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\treturn player position as text\n" +
                    "end tell");
            return (long)(Double.parseDouble(str)) * 1000;
        } catch (NumberFormatException e) {
            return 0L;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0l;
    }
    public  void setPlayBackPosition(long pos)
    {
        try {
            OSXUtils.runAppleCmd("tell application \"Spotify\"\n" +
                    "\tset player position to "+ pos/1000 +"\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public long getDuration()
    {
        try {
            return Long.parseLong(OSXUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\tduration of the current track\n" +
                    "end tell").trim().replace("\"", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getVolume() {
        try {
           return Integer.parseInt(OSXUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\tget sound volume\n" +
                    "end tell").trim());
        } catch (IOException e) {
           return -1;
        }
    }

    public  boolean isPlaying()
    {
        try {
           String str =  OSXUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\treturn player state\n" +
                    "end tell").trim();
           if(str.startsWith("pl"))
                return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public  void nextTrack()
    {
        try {
            OSXUtils.runAppleCmd("tell application \"Spotify\"\n" +
                    "\tnext track\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void previousTrack()
    {
        try {
            OSXUtils.runAppleCmd("tell application \"Spotify\"\n" +
                    "\tprevious track\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Artist> getTrackArtists() {
        return getPlayingTrack().getArtists();
    }

    public String getTrackArtist()
    {
        try {
            return OSXUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\tartist of current track\n" +
                    "end tell").trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public  String getTrackAlbum()
    {
        try {
            return OSXUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\talbum of current track\n" +
                    "end tell");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getTrackUri() {
        try {
            return OSXUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\treturn spotify url of the current track\n" +
                    "end tell").trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTrackName()
    {
        try {
            return OSXUtils.runAppleScript("tell application \"Spotify\"\n" +
                    "\tname of current track\n" +
                    "end tell").trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public Track getPlayingTrack() {
        return SpotifyUtils.getTrack(getTrackUri());
    }
}
