package com.pranav.movieapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pranav.movieapp.getmoviedetail.Genre
import com.pranav.movieapp.getmoviedetail.MovieDetailViewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.pranav.movieapp.getmoviedetail.CollectionInfo
import com.pranav.movieapp.getmoviedetail.ProductionCompany
import com.pranav.movieapp.getmoviedetail.SpokenLanguage
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun MovieDetailScreen(
    movieId: Int,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val movie by viewModel.movieDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetails(movieId)
    }

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        errorMessage != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Error: $errorMessage",
                    color = Color.Red,
                    onTextLayout = {}
                )
            }
        }

        movie != null -> {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                // Header Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = movie!!.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            onTextLayout = {}
                        )
                        Text(
                            text = movie!!.original_title,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray,
                            onTextLayout = {}
                        )
                    }

                    RatingBar(
                        rating = movie!!.vote_average / 2 ,
                        modifier = Modifier.size(80.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Basic Info Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoChip(
                        text = movie!!.release_date,
                        icon = Icons.Default.CalendarToday // Alternative to DateRange
                    )
                    InfoChip(
                        text = "${movie!!.runtime} min",
                        icon = Icons.Default.AccessTime // Alternative to Timer
                    )
                    InfoChip(
                        text = if (movie!!.adult) "18+" else "PG",
                        icon = if (movie!!.adult) Icons.Default.Warning else Icons.Default.Person // Alternative to ChildCare
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Backdrop Image
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w780${movie!!.backdrop_path}",
                    contentDescription = "Movie Backdrop",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tagline
                if (!movie!!.tagline.isNullOrEmpty()) {
                    Text(
                        text = "\"${movie!!.tagline}\"",
                        style = MaterialTheme.typography.titleMedium,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(bottom = 8.dp),
                        onTextLayout = {}

                    )
                }

                // Overview
                Text(
                    text = movie!!.overview,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp),
                    onTextLayout = {}
                )

                // Genres
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    movie!!.genres.forEach { genre ->
                        GenreChip(genre = genre)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Detailed Info Sections
                DetailSection(title = "Production") {
                    movie!!.production_companies.forEach { company ->
                        ProductionCompanyItem(company = company)
                    }
                }

                DetailSection(title = "Languages") {
                    movie!!.spoken_languages.forEach { language ->
                        LanguageItem(language = language)
                    }
                }

                // Collection Info
                movie!!.belongs_to_collection?.let { collection ->
                    DetailSection(title = "Collection") {
                        CollectionItem(collection = collection)
                    }
                }

                // Budget & Revenue
                DetailSection(title = "Financials") {
                    InfoRow(label = "Budget", value = formatCurrency(movie!!.budget))
                    InfoRow(label = "Revenue", value = formatCurrency(movie!!.revenue))
                }

                // External Links
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (!movie!!.homepage.isNullOrEmpty()) {
                        IconButton(
                            onClick = { movie!!.homepage?.let { openUrl(context, it) } },
                            modifier = Modifier.size(48.dp),
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            Icon(Icons.Default.Language, "Website")
                        }
                    }
                    if (!movie!!.imdb_id.isNullOrEmpty()) {
                        IconButton(
                            onClick = { openUrl(context, "https://www.imdb.com/title/${movie!!.imdb_id}") },
                            modifier = Modifier.size(48.dp),
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            Icon(Icons.Default.Movie, "IMDB")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductionCompanyItem(company: ProductionCompany) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        company.logo_path?.let { logoPath ->
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w200$logoPath",
                contentDescription = "Company Logo",
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text = company.name, onTextLayout = {})
    }
}

@Composable
fun LanguageItem(language: SpokenLanguage) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = language.name,
            modifier = Modifier.weight(1f),
            onTextLayout = {}
        )
        Text(
            text = "(${language.english_name})",
            color = Color.Gray,
            modifier = Modifier.weight(1f),
            onTextLayout = {}
        )
    }
}

@Composable
fun CollectionItem(collection: CollectionInfo) {
    Column {
        Text(
            text = collection.name,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            onTextLayout = {}
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            collection.poster_path?.let { posterPath ->
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w300$posterPath",
                    contentDescription = "Collection Poster",
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                )
            }
            collection.backdrop_path?.let { backdropPath ->
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w300$backdropPath",
                    contentDescription = "Collection Backdrop",
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                )
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold, onTextLayout = {})
        Text(text = value, onTextLayout = {})
    }
}

@Composable
fun RatingBar(
    rating: Double,  // Expected range: 0-5
    modifier: Modifier = Modifier,
    starSize: Dp = 16.dp,
    filledColor: Color = Color.Red,  // Gold color
    emptyColor: Color = Color.LightGray
) {
    Row(modifier = modifier) {
        val fullStars = rating.toInt()
        val hasHalfStar = rating - fullStars >= 0.5

        // Full stars
        repeat(fullStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Full star",
                tint = filledColor,
                modifier = Modifier.size(starSize)
            )
        }

        // Half star if needed
        if (hasHalfStar) {
            Icon(
                imageVector = Icons.Filled.StarHalf,
                contentDescription = "Half star",
                tint = filledColor,
                modifier = Modifier.size(starSize)
            )
        }

        // Empty stars
        repeat(5 - fullStars - if (hasHalfStar) 1 else 0) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Empty star",
                tint = emptyColor,
                modifier = Modifier.size(starSize)
            )
        }
    }
}

@Composable
fun InfoChip(text: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, onTextLayout = {})
    }
}

@Composable
fun GenreChip(genre: Genre) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Text(
            text = genre.name,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            onTextLayout = {}
        )
    }
}

@Composable
fun DetailSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp),
            onTextLayout = {}
        )
        content()
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

fun formatCurrency(amount: Int): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(amount)
}

fun openUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        // Handle error (e.g., show toast)
        Toast.makeText(context, "Couldn't open link", Toast.LENGTH_SHORT).show()
    }
}

