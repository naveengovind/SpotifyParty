package model

import java.net.URL
import java.io.*;
data class TrackInfo (val id: String = "", val name: String = "", val thumbnailURL: URL): Serializable