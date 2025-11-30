package com.github.siggisigmann.paragraphjump.action.jump

import com.github.siggisigmann.paragraphjump.JumpToEmptyLineAction

/**
 * Action to jump to the next paragraph (upwards).
 */
case class Up() extends JumpToEmptyLineAction(false) {
  // Required no-argument constructor for IntelliJ
}
