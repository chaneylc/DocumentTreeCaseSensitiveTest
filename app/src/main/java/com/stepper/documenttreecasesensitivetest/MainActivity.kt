package com.stepper.documenttreecasesensitivetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile

class MainActivity : AppCompatActivity() {

    private val launchOpenDocumentTree = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->

        //give access to a document tree uri
        val doc = DocumentFile.fromTreeUri(this, uri)

        //create first file "Test" <-- note title case
        doc?.createFile("*/*", "Test")

        //attempt to find lowercase "test" instead
        val name = "test"
        val child = doc?.findFile(name)

        //this is null, as it should be, intuitively...
        //even though DocumentProvider recommends using case-insensitive for DISPLAY_NAME queries
        Log.d("Test", "${child?.exists()}")

        //now create "test"
        doc?.createFile("*/*", "test")

        //this also returns null! very unintuitive
        val child2 = doc?.findFile("test")
        Log.d("Test", "${child2?.exists()}")

        //the second createFile seems to have a case-insensitive check to find duplicate names,
        //this leads to the creation of "test (1)"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launchOpenDocumentTree.launch(null)
    }
}