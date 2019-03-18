package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddReviewCommand;
import seedu.address.logic.commands.EditReviewCommand;
import seedu.address.logic.parser.exceptions.ParseException;

import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REVIEWENTRY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REVIEWRATING;

import seedu.address.logic.commands.EditReviewCommand.EditReviewDescriptor;


/**
 * Parses input arguments and creates a new EditReviewCommand object
 */
public class EditReviewCommandParser implements Parser<EditReviewCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditReviewCommand
     * and returns an EditReviewCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditReviewCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_REVIEWENTRY, PREFIX_REVIEWRATING);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddReviewCommand.MESSAGE_USAGE), pe);
        }

        EditReviewDescriptor editReviewDescriptor = new EditReviewDescriptor();
        if (argMultimap.getValue(PREFIX_REVIEWENTRY).isPresent()) {
            editReviewDescriptor.setEntry(ParserUtil.parseEntry(argMultimap.getValue(PREFIX_REVIEWENTRY).get()));
        }
        if (argMultimap.getValue(PREFIX_REVIEWRATING).isPresent()) {
            editReviewDescriptor.setRating(ParserUtil.parseRating(argMultimap.getValue(PREFIX_REVIEWRATING).get()));
        }

        if (!editReviewDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditReviewCommand.MESSAGE_NOT_EDITED);
        }

        return new EditReviewCommand(index, editReviewDescriptor);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
