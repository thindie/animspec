package com.thindie.animspecs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.thindie.animspecs.AnimatedTextDefaults.animSpec
import com.thindie.animspecs.ui.theme.AnimspecsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var text by remember {
                mutableStateOf("")
            }
            AnimspecsTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    AnimatedText(text = text)
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { text += listOf("a", "b", "c").shuffled()[1] }) {
                        Text(text = "Append")
                    }
                    Button(onClick = { text = text.dropLast(1) }) {
                        Text(text = "erase")
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
) {
    var animatedContentProps by remember { mutableStateOf(Params()) }
    val props = Params(text = text.dropLast(1), symbol = text.takeLast(1))
    animatedContentProps = props
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = animatedContentProps.text,
            modifier = Modifier,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = style
        )

        AnimatedContent(
            targetState = animatedContentProps,
            transitionSpec = animSpec(),
            label = "last_animated_symbol_content"
        ) { _ ->
            Text(
                modifier = Modifier,
                text = animatedContentProps.symbol,
                color = color,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign,
                lineHeight = lineHeight,
                overflow = overflow,
                softWrap = softWrap,
                maxLines = maxLines,
                minLines = minLines,
                style = style,
                onTextLayout = onTextLayout
            )
        }
    }
}

@Immutable
private data class Params(
    val text: String = "",
    val symbol: String = "",
)

@Immutable
object AnimatedTextDefaults {

    fun <T> animSpec(): AnimatedContentTransitionScope<T>.() -> ContentTransform {
        val spec =
            slideInVertically(
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                initialOffsetY = { height -> height }
            ) + fadeIn() togetherWith slideOutVertically(
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                targetOffsetY = { height -> -height }
            ) + fadeOut()

        return { spec.using(SizeTransform(clip = false)) }
    }
}
