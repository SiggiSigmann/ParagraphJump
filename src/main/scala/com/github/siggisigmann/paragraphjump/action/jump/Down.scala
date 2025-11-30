package com.github.siggisigmann.paragraphjump.action.jump

import com.github.siggisigmann.paragraphjump.JumpToEmptyLineAction

/**
 * Action to jump to the next paragraph (downwards).
 */
case class Down() extends JumpToEmptyLineAction(true){
  // Required no-argument constructor for IntelliJ
}
