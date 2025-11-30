package com.github.siggisigmann.paragraphjump

import com.github.siggisigmann.paragraphjump.ParagraphUtil
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.editor.{Document, Editor, ScrollType}
import com.intellij.openapi.util.TextRange

/**
 * Abstract action to jump the caret to the next or previous paragraph in the editor.
 * The direction of the jump is determined by the concrete implementation.
 */
abstract class JumpToEmptyLineAction(moveDown: Boolean) extends AnAction {
  private final val direction = if (moveDown) 1 else -1

  /**
   * Controls the visibility and enabled state of this action.
   * The action is only enabled if an editor is available.
   *
   * @param e The action event.
   */
  override def update(e: AnActionEvent): Unit = {
    val editorOpt = Option(e.getData(CommonDataKeys.EDITOR))
    e.getPresentation.setEnabled(editorOpt.nonEmpty)
  }

  /**
   * Executes the action to jump to the next/previous paragraph.
   * If a empty paragraph is found, the caret is moved to the edge of the block.
   *
   * @param e The action event.
   */
  override def actionPerformed(e: AnActionEvent): Unit = {
    Option(e.getData(CommonDataKeys.EDITOR)).map{ implicit editor =>
      //get information from event
      implicit val doc: Document = editor.getDocument
      val caret = editor.getCaretModel

      //determine where to start searching
      val currentLine = doc getLineNumber caret.getOffset
      val lineToStartSearch = currentLine + direction

      //find the line to jump to
      val lineToJump = if (ParagraphUtil.isLineEmpty(lineToStartSearch))
          getEndOfEmptyParagrahp(lineToStartSearch)
        else
          getNextEmptyLineAfterParagraph(lineToStartSearch)
      val caracteroffset = doc.getLineStartOffset(lineToJump)
      moveCaret(caracteroffset)
    }
  }

  /**
    * Finds the end of an empty paragraph in the specified direction.
    *
    * @param lineToStartSearch
    * @param doc
    * @return
    */
  def getEndOfEmptyParagrahp(lineToStartSearch: Int)(implicit doc: Document): Int={
    lazy val lastLine = doc.getLineCount - 1
    lazy val startOrEndLine = if (moveDown) lastLine else 0

    ParagraphUtil.findNextLineWithContent(lineToStartSearch, direction)
      .map(_ - direction) // one step back so we land on the last empty line
      .getOrElse(startOrEndLine)
  }

  /**
   * Finds the next empty line after a paragraph in the specified direction.
   *
   * @param lineToStartSearch The line to start searching from.
   * @param doc               The document to search in.
   * @return The line number of the next empty line after a paragraph.
   */
  def getNextEmptyLineAfterParagraph(lineToStartSearch: Int)(implicit doc: Document): Int={
    lazy val lastLine = doc.getLineCount - 1
    lazy val startOrEndLine = if (moveDown) lastLine else 0

    ParagraphUtil.findNextEmptyLine(lineToStartSearch, direction)
      .getOrElse(startOrEndLine)
  }

  /**
   * Moves the caret to the specified offset and scrolls the editor to make the caret visible.
   *
   * @param editor The editor.
   * @param offset The offset to move the caret to.
   */
  private def moveCaret(offset: Int)(implicit editor: Editor): Unit = {
    editor.getCaretModel.moveToOffset(offset)
    editor.getScrollingModel.scrollToCaret(ScrollType.CENTER)
  }
}
