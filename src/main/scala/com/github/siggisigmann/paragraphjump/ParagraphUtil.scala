package com.github.siggisigmann.paragraphjump

import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange

object ParagraphUtil {
  /** Checks if a line is empty (contains only whitespace). */
  def isLineEmpty(line: Int)(implicit doc: Document): Boolean = {
    if (line < 0 || line >= doc.getLineCount) return false
    val start = doc.getLineStartOffset(line)
    val end = doc.getLineEndOffset(line)
    val text = doc.getText(new TextRange(start, end)).trim
    text.isEmpty
  }

  /** Finds the start and end line of the paragraph containing the given line. */
  def getParagraphBounds(line: Int)(implicit doc: Document): (Int, Int) = {
    val totalLines = doc.getLineCount
    val start = Iterator.iterate(line)(_ - 1)
      .takeWhile(i => i > 0 && !isLineEmpty(i - 1))
      .foldLeft(line)((_, i) => i)
    val end = Iterator.iterate(line)(_ + 1)
      .takeWhile(i => i < totalLines - 1 && !isLineEmpty(i + 1))
      .foldLeft(line)((_, i) => i)
    (start, end)
  }

  /** Finds the next empty line in the specified direction. */
  def findNextEmptyLine(lineToStartSearch: Int, direction: Int)(implicit doc: Document): Option[Int] = {
    val totalLines = doc.getLineCount
    Iterator.iterate(lineToStartSearch)(_ + direction)
      .takeWhile(i => i >= 0 && i < totalLines)
      .find(isLineEmpty(_))
  }

  /** Finds the next line with content in the specified direction. */
  def findNextLineWithContent(lineToStartSearch: Int, direction: Int)(implicit doc: Document): Option[Int] = {
    val totalLines = doc.getLineCount
    Iterator.iterate(lineToStartSearch)(_ + direction)
      .takeWhile(i => i >= 0 && i < totalLines)
      .find(i => !isLineEmpty(i))
  }
}
